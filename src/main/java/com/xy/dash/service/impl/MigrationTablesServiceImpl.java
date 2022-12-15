package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.mapper.MigrationTablesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.MigrationFieldsService;
import com.xy.dash.service.MigrationTablesService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.StringUtil;
import com.xy.dash.vo.TemplatesDataSources;
import com.xy.dash.vo.TemplatesTables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据迁移表配置 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MigrationTablesServiceImpl extends ServiceImpl<MigrationTablesMapper, MigrationTables> implements MigrationTablesService {

    @Autowired
    private MigrationFieldsService migrationFieldsService;

    @Override
    public List<TemplatesTables> getTemplatesTables(List<TemplatesDataSources> templatesDataSources) {
        List<TemplatesTables> templatesTables = new ArrayList<>();
        List<Long> ids = templatesDataSources.stream().map(TemplatesDataSources::getId).collect(Collectors.toList());
        Map<Long, TemplatesDataSources> dataSourcesMap = templatesDataSources.stream().collect(Collectors.toMap(TemplatesDataSources::getId, Function.identity()));
        List<MigrationTables> migrationTables = list(new LambdaQueryWrapper<MigrationTables>().in(MigrationTables::getMigrationSourcesId, ids));
        /**
         * 按数据库表名分组 如果数据库名重复 生成的类名需要加上唯一名称（uniqueName）
         * 1. 分组 2.筛选长度 3.getkey
         */
        List<String> tableNames = migrationTables.stream()
                .collect(Collectors.groupingBy(MigrationTables::getName))
                .entrySet().stream().filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());


        migrationTables.forEach(e -> {
            TemplatesDataSources templatesDataSource = dataSourcesMap.get(e.getMigrationSourcesId());
            TemplatesTables templatesTable = new TemplatesTables();
            templatesTable.setUniqueName(templatesDataSource.getUniqueName());
            templatesTable.setType(templatesDataSource.getType());
            templatesTable.setTemplatesFields(migrationFieldsService.getTemplatesFields(e.getId()));
            BeanUtil.copyProperties(e, templatesTable);

            String name = StringUtil.lineToHump(templatesTable.getName());
            String entityName = StringUtil.upperCase(name);

            if (tableNames.contains(templatesTable.getName())) {
                entityName = StringUtil.upperCase(templatesTable.getUniqueName().concat(entityName));
            }

            String implName = entityName.concat("ServiceImpl");
            String serviceName = entityName.concat("Service");
            String mapperName = entityName.concat("Mapper");
            String handlerName = entityName.concat("Handler");

            templatesTable.setEntityName(entityName);
            templatesTable.setImplName(implName);
            templatesTable.setServiceName(serviceName);
            templatesTable.setMapperName(mapperName);
            templatesTable.setHandlerName(handlerName);

            templatesTable.setLowerEntityName(StringUtil.lowerCase(entityName));
            templatesTable.setLowerImplName(StringUtil.lowerCase(implName));
            templatesTable.setLowerServiceName(StringUtil.lowerCase(serviceName));
            templatesTable.setLowerMapperName(StringUtil.lowerCase(mapperName));
            templatesTable.setLowerHandlerName(StringUtil.lowerCase(handlerName));
            templatesTables.add(templatesTable);
        });

        // 迁移表 获取来源类名
        templatesTables.stream().filter(TemplatesTables::getMigration).forEach(e -> {
            Optional<TemplatesTables> first = templatesTables.stream().filter(templatesTable -> {
                return templatesTable.getUniqueName().equals(e.getSourceUniqueName()) && e.getSourceName().equals(templatesTable.getName());
            }).findFirst();
            if (first.isPresent()) {
                e.setSourceName(first.get().getEntityName());
            }
        });
        return templatesTables;
    }
}
