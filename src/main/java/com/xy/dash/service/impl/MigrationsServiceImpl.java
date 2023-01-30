package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.dash.aspect.GlobalLog;
import com.xy.dash.converts.DataSourceConvert;
import com.xy.dash.entity.*;
import com.xy.dash.enums.DbColumnType;
import com.xy.dash.mapper.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.*;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.StringUtil;
import com.xy.dash.utli.exception.ServiceException;
import com.xy.dash.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据迁移配置 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@Service
public class MigrationsServiceImpl extends ServiceImpl<MigrationsMapper, Migrations> implements MigrationsService {

    @Autowired
    private TemplatesService templatesService;
    @Autowired
    private MigrationDataSourcesService migrationDataSourcesService;
    @Autowired
    private MigrationTablesService migrationTablesService;
    @Autowired
    private MigrationFieldsService migrationFieldsService;
    @Autowired
    private DataSourcesMapper dataSourcesMapper;


    @Override
    public void generatingCode(Long id) {
        // 获取数据迁移配置
        Migrations migrations = getById(id);
        if (migrations == null) {
            throw new ServiceException("数据迁移配置不存在！");
        }
        migrations.setThreadCount(migrations.getThreadCount() == null ? 500 : migrations.getThreadCount());
        // 获取数据源配置
        List<TemplatesDataSources> templatesDataSources = migrationDataSourcesService.getTemplatesDataSources(id);
        // 获取表配置
        List<TemplatesTables> templatesTables = migrationTablesService.getTemplatesTables(templatesDataSources);

        // todo 保存到代码记录
        String name = "data-migration".concat(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()).concat((int) ((Math.random() * 9 + 1) * 1000) + ""));

        templatesService.create(TemplatesAdd.builder().migrations(migrations).templatesTables(templatesTables).migrationDataSources(templatesDataSources).fileName(name).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Migrations migrations) {
        List<MigrationDataSources> migrationDataSources = migrations.getMigrationDataSources();
        List<MigrationTables> migrationTables = new ArrayList<>();
        List<MigrationFields> migrationFields = new ArrayList<>();

        verify(migrations);
        saveOrUpdate(migrations);

        migrationDataSources.forEach(e -> e.setMigrationsId(migrations.getId()));
        migrationDataSourcesService.saveOrUpdateBatch(migrationDataSources);

        migrationDataSources.forEach(e -> {
            List<MigrationTables> migrationTablesList = e.getMigrationTables();
            migrationTablesList.forEach(migrationTable -> {
                migrationTable.setMigrationSourcesId(e.getId());
                migrationTable.setType(e.getType());
            });
            migrationTables.addAll(migrationTablesList);
        });
        migrationTablesVerify(migrationTables);
        migrationTablesService.saveOrUpdateBatch(migrationTables);

        migrationTables.forEach(e -> {
            List<MigrationFields> migrationFieldsList = e.getMigrationFields();
            migrationFieldsList.forEach(migrationField -> {
                migrationField.setTableId(e.getId());
                migrationField.setType(e.getType());
            });
            migrationFields.addAll(migrationFieldsList);
        });
        migrationFieldConvert(migrationFields);
        migrationFieldsService.saveOrUpdateBatch(migrationFields);
    }

    private void migrationTablesVerify(List<MigrationTables> migrationTables) {
        List<Integer> collect = migrationTables.stream().filter(MigrationTables::getMigration).map(e -> {
            Integer position = e.getPosition();
            if (position == null) {
                throw new ServiceException("权重不能为空！");
            }
            if (e.getSourceUniqueName() == null && e.getSourceName() == null) {
                throw new ServiceException("迁移表来源数据不能为空！");
            }
            return position;
        }).collect(Collectors.toList());
        if (collect.size() != collect.stream().distinct().count()) {
            throw new ServiceException("权重不能重复！");
        }
    }

    private void verify(Migrations migrations) {
        List<MigrationDataSources> migrationDataSources = migrations.getMigrationDataSources();

        Set<String> uniqueNames = migrationDataSources.stream().map(MigrationDataSources::getUniqueName).collect(Collectors.toSet());
        List<Long> sourcesIds = migrationDataSources.stream()
                .filter(e -> e.getType().equals(1))
                .map(MigrationDataSources::getId).collect(Collectors.toList());

        if (Boolean.TRUE.equals(migrations.getStartThread()) && migrations.getThreadCount() == null) {
            throw new ServiceException("线程数量不能为空！");
        }
        if (migrations.getSqlSpliec() && !CollectionUtils.isEmpty(sourcesIds)) {
            throw new ServiceException("sqlserver不支持sql拼接！");
        }
        if (uniqueNames.size() != migrationDataSources.size()) {
            throw new ServiceException("同一配置，数据源名称不能重复！");
        }

    }

    private void migrationFieldConvert(List<MigrationFields> migrationFieldsList) {
        migrationFieldsList.forEach(e -> {
            DbColumnType dbColumnType = DataSourceConvert.getDbColumnType(e.getType(), e.getFieldType());
            e.setPropertyName(StringUtil.upperCase(StringUtil.lineToHump(e.getFieldName())));
            e.setPkg(dbColumnType.getPkg());
            e.setPropertyType(dbColumnType.getType());
        });
    }
}
