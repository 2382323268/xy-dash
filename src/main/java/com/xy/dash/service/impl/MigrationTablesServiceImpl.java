package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xy.dash.entity.MigrationJoinTables;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.mapper.MigrationTablesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.MigrationFieldsService;
import com.xy.dash.service.MigrationJoinTablesService;
import com.xy.dash.service.MigrationTablesService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.StringUtil;
import com.xy.dash.vo.TemplatesDataSources;
import com.xy.dash.vo.TemplatesFields;
import com.xy.dash.vo.TemplatesJoinTables;
import com.xy.dash.vo.TemplatesTables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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
    @Autowired
    private MigrationJoinTablesService migrationJoinTablesService;

    @Override
    public List<TemplatesTables> getTemplatesTables(List<TemplatesDataSources> templatesDataSources) {
        List<TemplatesTables> templatesTables = new ArrayList<>();
        List<Long> ids = templatesDataSources.stream().map(TemplatesDataSources::getId).collect(Collectors.toList());
        Map<Long, TemplatesDataSources> dataSourcesMap = templatesDataSources.stream().collect(Collectors.toMap(TemplatesDataSources::getId, Function.identity()));
        List<MigrationTables> migrationTables = list(new LambdaQueryWrapper<MigrationTables>().in(MigrationTables::getMigrationSourcesId, ids));
        /**
         * 按数据库表名分组 如果数据库名重复 生成的类名需要加上唯一名称（uniqueName）
         * 1. 分组 2.筛选长度 3.get key
         */
        List<String> tableNames = migrationTables.stream()
                .collect(Collectors.groupingBy(MigrationTables::getName))
                .entrySet().stream().filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());


        migrationTables.forEach(e -> {
            TemplatesTables templatesTable = getTemplatesTable(e, dataSourcesMap, tableNames);
            // todo 修改成批量查询
            templatesTable.setTemplatesFields(migrationFieldsService.getTemplatesFields(e.getId(), tableNames));
            templatesTables.add(templatesTable);
        });

        // 迁移表 获取来源类名
        templatesTables.stream().filter(TemplatesTables::getMigration).forEach(e -> {
            Optional<TemplatesTables> first = templatesTables.stream().filter(templatesTable -> {
                return templatesTable.getUniqueName().equals(e.getSourceUniqueName()) && e.getSourceName().equals(templatesTable.getName());
            }).findFirst();
            if (first.isPresent()) {
                TemplatesTables firstTable = first.get();

                e.setSourceName(firstTable.getEntityName());
                String handlerName = firstTable.getEntityName().concat("To").concat(e.getEntityName()).concat("Handler");
                e.setHandlerName(handlerName);
                e.setLowerHandlerName(StringUtil.lowerCase(handlerName));

                if (CollectionUtils.isNotEmpty(e.getQueryMap())) {
                    e.setQueryMapType(getQueryMapType(e.getQueryMap(), firstTable));
                }

                firstTable.getTemplatesFields().forEach(f -> {
                    if (f.getStatus() != 0) {
                        String name = StringUtil.upperCase(StringUtil.lineToHump(f.getFieldName()));
                        e.getTemplatesFields().forEach(x -> {
                            if (x.getValue() != null && x.getValue().equals(name)) {
                                x.setValue(f.getPropertyName());
                            }
                        });
                    }
                });
            }
        });
        // 迁移表 获取join表
        setJoinTable(templatesTables);

        return templatesTables;
    }

    private TemplatesTables getTemplatesTable(MigrationTables e, Map<Long, TemplatesDataSources> dataSourcesMap, List<String> tableNames) {
        // 表名称是否拼上唯一名称
        boolean isUniqueName = tableNames.contains(e.getName());

        TemplatesDataSources templatesDataSource = dataSourcesMap.get(e.getMigrationSourcesId());
        TemplatesTables templatesTable = new TemplatesTables();
        BeanUtil.copyProperties(e, templatesTable);
        templatesTable.setUniqueName(templatesDataSource.getUniqueName());
        templatesTable.setType(templatesDataSource.getType());

        String name = StringUtil.lineToHump(templatesTable.getName());
        String entityName = StringUtil.upperCase(name);

        if (isUniqueName) {
            entityName = StringUtil.upperCase(templatesTable.getUniqueName().concat(entityName));
        }

        String implName = entityName.concat("ServiceImpl");
        String serviceName = entityName.concat("Service");
        String mapperName = entityName.concat("Mapper");

        templatesTable.setEntityName(entityName);
        templatesTable.setImplName(implName);
        templatesTable.setServiceName(serviceName);
        templatesTable.setMapperName(mapperName);

        templatesTable.setLowerEntityName(StringUtil.lowerCase(entityName));
        templatesTable.setLowerImplName(StringUtil.lowerCase(implName));
        templatesTable.setLowerServiceName(StringUtil.lowerCase(serviceName));
        templatesTable.setLowerMapperName(StringUtil.lowerCase(mapperName));
        return templatesTable;
    }

    private void setJoinTable(List<TemplatesTables> templatesTables) {
        Map<Long, List<MigrationJoinTables>> migrationJoinTableMap = getMigrationJoinTableMap(templatesTables);

        templatesTables.stream().filter(TemplatesTables::getMigration).forEach(e -> {
            List<String> list = e.getTemplatesFields().stream().map(TemplatesFields::getValueMap).collect(Collectors.toList());
            List<MigrationJoinTables> migrationJoinTables = migrationJoinTableMap.get(e.getId());

            if (CollectionUtils.isNotEmpty(migrationJoinTables)) {
                List<TemplatesJoinTables> collect = migrationJoinTables.stream().map(v -> {
                    TemplatesTables templatesTable = templatesTables.stream().filter(x -> x.getId().equals(v.getTableId())).findFirst().orElseGet(TemplatesTables::new);
                    TemplatesJoinTables templatesJoinTables = BeanUtil.copyProperties(v, TemplatesJoinTables.class);
                    BeanUtil.copyProperties(templatesTable, templatesJoinTables);

                    String joinFiled = StringUtil.lineToHump(templatesJoinTables.getJoinFiled());
                    String filed = StringUtil.lineToHump(templatesJoinTables.getField());

                    templatesJoinTables.setJoinFiled(StringUtil.upperCase(joinFiled));
                    templatesJoinTables.setLowerJoinFiled(StringUtil.lowerCase(joinFiled));
                    templatesJoinTables.setField(StringUtil.upperCase(filed));
                    templatesJoinTables.setLowerField(StringUtil.lowerCase(filed));

                    TemplatesTables firstTable = templatesTables.stream().filter(x -> x.getId().equals(v.getTableId())).findFirst().get();
                    templatesJoinTables.setQueryMap(v.getQueryMap());
                    if (CollectionUtils.isNotEmpty(v.getQueryMap())) {
                        templatesJoinTables.setQueryMapType(getQueryMapType(templatesJoinTables.getQueryMap(), firstTable));
                    }
                    return templatesJoinTables;
                }).collect(Collectors.toList());

                // 过滤掉没有用到的 jointable
                collect = collect.stream().filter(v -> {
                    return list.contains(v.getLowerEntityName() + "By" + v.getField());
                }).collect(Collectors.toList());
                // 连接表去重
                e.setDistinctTemplatesJoinTables(collect.stream().collect(collectingAndThen(toCollection(() ->
                        new TreeSet<>(Comparator.comparing(TemplatesJoinTables::getTableId))), ArrayList::new)));
                // 连接属性去重
                e.setDistinctTemplatesFields(e.getTemplatesFields().stream()
                        .filter(v -> StringUtil.isNotEmpty(v.getValueMap()))
                        .collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(Comparator.comparing(v -> v.getValue() + v.getValueMap()))), ArrayList::new)));
                e.setTemplatesJoinTables(collect);
            }
        });
    }

    private Map<Long, List<MigrationJoinTables>> getMigrationJoinTableMap(List<TemplatesTables> templatesTables) {
        List<Long> tableIds = templatesTables.stream().filter(TemplatesTables::getMigration).map(TemplatesTables::getId).collect(Collectors.toList());
        List<MigrationJoinTables> migrationJoinTables = migrationJoinTablesService.list(Wrappers.<MigrationJoinTables>lambdaQuery().in(MigrationJoinTables::getMigrationTableId, tableIds));
        if (CollectionUtils.isEmpty(migrationJoinTables)) {
            return new HashMap<>();
        }
        return migrationJoinTables.stream().collect(Collectors.groupingBy(MigrationJoinTables::getMigrationTableId));
    }

    private Map<String, String> getQueryMapType(Map<String, String> queryMap, TemplatesTables firstTable) {
        Map<String, String> queryMapType = new HashMap<>();

        for (String k : queryMap.keySet()) {
            String[] split = k.split("_&");
            if (StringUtil.isBlank(split[0])) {
                continue;
            }
            if (Arrays.asList("in", "notIn").contains(split[1])) {
                queryMap.put(k, queryMap.get(k).replace(",", "\",\""));
            }
            String propertyType = firstTable.getTemplatesFields().stream().filter(x -> x.getFieldName().equals(split[0])).findFirst().get().getPropertyType();
            queryMapType.put(k, propertyType);
        }
        return queryMapType;
    }
}
