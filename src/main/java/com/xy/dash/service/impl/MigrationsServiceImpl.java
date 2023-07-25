package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Function;
import com.xy.dash.converts.DataSourceConvert;
import com.xy.dash.entity.*;
import com.xy.dash.enums.DbColumnType;
import com.xy.dash.mapper.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.*;
import com.xy.dash.utli.*;
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
    private MigrationCodesService migrationCodesService;
    @Autowired
    private MigrationFieldsService migrationFieldsService;
    @Autowired
    private MigrationJoinTablesService migrationJoinTablesService;
    @Autowired
    private DataSourcesMapper dataSourcesMapper;


    @Override
    public void generatingCode(Long id, String remark) {
        // 获取数据迁移配置
        Migrations migrations = getById(id);
        if (migrations == null) {
            throw new ServiceException("数据迁移配置不存在！");
        }
        // 获取数据源配置
        List<TemplatesDataSources> templatesDataSources = migrationDataSourcesService.getTemplatesDataSources(id);
        // 获取表配置
        List<TemplatesTables> templatesTables = migrationTablesService.getTemplatesTables(templatesDataSources);
        templatesTables.forEach(e -> {
            e.setSqlSpliec(migrations.getSqlSpliec());
            e.setMigrationType(migrations.getType());
        });
        String name = "data-migration".concat(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()).concat((int) ((Math.random() * 9 + 1) * 1000) + ""));
        MigrationCodes migrationCodes = MigrationCodes.builder().migrationId(id).fileName(name).remark(remark).runCount(0).build();
        migrationCodesService.save(migrationCodes);

        templatesService.create(TemplatesAdd.builder().migrations(migrations).templatesTables(templatesTables).migrationDataSources(templatesDataSources).fileName(name).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Migrations migrations) {
        List<MigrationDataSources> migrationDataSources = migrations.getMigrationDataSources();
        List<MigrationTables> migrationTables = new ArrayList<>();
        List<MigrationFields> migrationFields = new ArrayList<>();
        List<MigrationJoinTables> migrationJoinTables = new ArrayList<>();

        // 批量删除
        batchDelete(migrations.getId(), false);
        // 校验
        verify(migrations);
        // 保存数据迁移配置
        saveOrUpdate(migrations);

        migrationDataSources.forEach(e -> e.setMigrationsId(migrations.getId()));
        // 保存库配置
        migrationDataSourcesService.saveBatch(migrationDataSources);

        migrationDataSources.forEach(e -> {
            List<MigrationTables> migrationTablesList = e.getMigrationTables();
            migrationTablesList.forEach(migrationTable -> {
                migrationTable.setMigrationSourcesId(e.getId());
                migrationTable.setUniqueName(e.getUniqueName());
                migrationTable.setType(e.getType());
            });
            migrationTables.addAll(migrationTablesList);
        });
        migrationTablesVerify(migrationTables, migrations);
        // 保存表配置
        migrationTablesService.saveBatch(migrationTables);

        migrationTables.forEach(e -> {
            List<MigrationFields> migrationFieldsList = e.getMigrationFields();
            migrationFieldsList.forEach(migrationField -> {
                migrationField.setTableId(e.getId());
                migrationField.setType(e.getType());
            });

            if (!CollectionUtils.isEmpty(e.getMigrationJoinTables())) {
                e.getMigrationJoinTables().forEach(join -> {
                    join.setMigrationTableId(e.getId());
                    join.setSelectList(join.getSelectList().stream().map(v -> StringUtil.upperCase(StringUtil.lineToHump(v))).collect(Collectors.toList()));
                    join.setTableId(migrationTables.stream()
                            .filter(v -> v.getName().equals(join.getName()) && v.getUniqueName().equals(join.getUniqueName()))
                            .findFirst().get().getId());
                });
                migrationJoinTables.addAll(e.getMigrationJoinTables());
            }
            migrationFields.addAll(migrationFieldsList);
        });
        migrationFieldConvert(migrationFields);

        Optional.ofNullable(migrationJoinTables).ifPresent(e -> migrationJoinTablesService.saveBatch(e));
        // 保存属性配置
        migrationFieldsService.saveBatch(migrationFields);
    }

    public void batchDelete(Long id) {
        batchDelete(id, true);
    }

    public void batchDelete(Long id, boolean delete) {
        if (id == null) {
            return;
        }
        if (delete) {
            removeById(id);
        }
        OperationData<MigrationDataSources, Long> operationData = OperationData.create(MigrationDataSources.class, id, MigrationDataSources::getMigrationsId);
        operationData.ifPresent(e -> {
            List<Long> dataSourceIds = e.stream().map(MigrationDataSources::getId).collect(Collectors.toList());
            migrationDataSourcesService.removeByIds(dataSourceIds);

            OperationData.create(MigrationTables.class, dataSourceIds, MigrationTables::getMigrationSourcesId).ifPresent(v -> {
                List<Long> tableIds = v.stream().map(MigrationTables::getId).collect(Collectors.toList());
                List<Long> fieldIds = v.stream().map(MigrationTables::getMigrationFields).filter(ObjectUtils::isNotEmpty).flatMap(Collection::stream).map(MigrationFields::getId).collect(Collectors.toList());
                List<Long> joinIds = v.stream().map(MigrationTables::getMigrationJoinTables).filter(ObjectUtils::isNotEmpty).flatMap(Collection::stream).map(MigrationJoinTables::getId).collect(Collectors.toList());

                migrationTablesService.removeByIds(tableIds);
                migrationJoinTablesService.removeByIds(joinIds);
                migrationFieldsService.removeByIds(fieldIds);
            });


        });

    }

    @Override
    public IPage<Migrations> queryPage(Map<String, Object> param, Query query) {
        QueryWrapper<Migrations> queryWrapper = Condition.getQueryWrapper(param, Migrations.class);
        return page(Condition.getPage(query), queryWrapper);
    }

    @Override
    public Migrations details(Long id) {
        Migrations migrations = getById(id);
        // 数据源数据
        OperationData<MigrationDataSources, Long> migrationDataSources = getMigrationDataSources(id);
        Map<Long, MigrationDataSources> dataSourcesMap = migrationDataSources.getMap();
        List<MigrationDataSources> migrationDataSourcesList = migrationDataSources.getList();
        List<Long> migrationDataSourceIds = migrationDataSources.getFields();
        OperationData<MigrationDataSources, Long> migrationDataSourcesBySourceId = OperationData.create(migrationDataSources, MigrationDataSources::getSourceId);
        List<Long> sourceIds = migrationDataSourcesBySourceId.getFields();

        // 表数据
        OperationData<MigrationTables, Long> migrationTable = getMigrationTables(migrationDataSourceIds);
        List<MigrationTables> migrationTables = migrationTable.getList();
        List<Long> migrationTableIds = migrationTable.getFields();
        Map<Long, MigrationTables> migrationTablesMap = migrationTable.getMap();

        // 连接表数据
        OperationData<MigrationJoinTables, Long> migrationJoinTableByMigrationTableId = getMigrationJoinTables(migrationTableIds);
        Map<Long, List<MigrationJoinTables>> migrationJoinTablesMap = migrationJoinTableByMigrationTableId.getGroupMap();

        // 字段数据
        OperationData<MigrationFields, Long> operationData = getMigrationFieldsList(migrationTableIds);
        Map<Long, List<MigrationFields>> migrationFieldsListGroupMap = operationData.getGroupMap();


        Map<Long, Integer> map = dataSourcesMapper.selectBatchIds(sourceIds).stream().collect(Collectors.toMap(DataSources::getId,
                DataSources::getType));

        migrationDataSourcesList.forEach(e -> {
            List<MigrationTables> migrationTablesList = migrationTables.stream().filter(v -> v.getMigrationSourcesId().equals(e.getId())).collect(Collectors.toList());
            e.setMigrationTables(migrationTablesList);
            e.setType(map.get(e.getSourceId()));
            e.getMigrationTables().forEach(v -> {
                List<MigrationFields> migrationFieldsList = migrationFieldsListGroupMap.get(v.getId());
                if (!CollectionUtils.isEmpty(migrationFieldsList)) {
                    migrationFieldsList.forEach(x -> {
                        x.setValue(StringUtil.humpToLine(x.getValue()));
                    });
                    v.setMigrationFields(migrationFieldsList);
                }

                List<MigrationJoinTables> migrationJoinTables = migrationJoinTablesMap.get(v.getId());
                if (!CollectionUtils.isEmpty(migrationJoinTables)) {
                    migrationJoinTables.forEach(x -> {
                        MigrationTables tables = migrationTablesMap.get(x.getTableId());
                        MigrationDataSources sources = dataSourcesMap.get(tables.getMigrationSourcesId());
                        x.setName(tables.getName());
                        x.setSelectList(x.getSelectList() == null ? null : x.getSelectList().stream().map(z -> StringUtil.humpToLine(StringUtil.lowerCase(z))).collect(Collectors.toList()));
                        x.setUniqueName(sources.getUniqueName());
                        x.setDatabaseName(sources.getDatabaseName());
                        x.setDataSourceId(e.getSourceId());
                    });
                    v.setMigrationJoinTables(migrationJoinTables);
                }
                v.setType(e.getType());
            });
        });
        migrations.setMigrationDataSources(migrationDataSourcesList);
        return migrations;
    }

    private OperationData<MigrationFields, Long> getMigrationFieldsList(List<Long> migrationTableIds) {
        List<MigrationFields> migrationFieldsList = migrationFieldsService.list(Wrappers.<MigrationFields>lambdaQuery().in(MigrationFields::getTableId, migrationTableIds));
        return OperationData.create(migrationFieldsList, MigrationFields::getTableId);
    }

    private OperationData<MigrationJoinTables, Long> getMigrationJoinTables(List<Long> migrationTableIds) {
        List<MigrationJoinTables> migrationJoinTables = migrationJoinTablesService.list(Wrappers.<MigrationJoinTables>lambdaQuery().in(MigrationJoinTables::getMigrationTableId, migrationTableIds));
        return OperationData.create(migrationJoinTables, MigrationJoinTables::getMigrationTableId);
    }

    private OperationData<MigrationTables, Long> getMigrationTables(List<Long> migrationDataSourceIds) {
        List<MigrationTables> migrationTables = migrationTablesService.list(Wrappers.<MigrationTables>lambdaQuery()
                .in(MigrationTables::getMigrationSourcesId, migrationDataSourceIds));
        return OperationData.create(migrationTables, MigrationTables::getId);
    }

    private OperationData<MigrationDataSources, Long> getMigrationDataSources(Long id) {
        List<MigrationDataSources> list = migrationDataSourcesService.list(Wrappers.<MigrationDataSources>lambdaQuery().eq(MigrationDataSources::getMigrationsId, id));
        return OperationData.<MigrationDataSources, Long>create(list, MigrationDataSources::getId);
    }

    private void migrationTablesVerify(List<MigrationTables> migrationTables, Migrations migrations) {
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
        if (migrations.getIsDel()) {
            long count = migrationTables.stream().filter(e -> e.getIdType() != null && e.getIdType() != 1).count();
            if (count > 0) {
                throw new ServiceException("id生成策略不为映射,无法删除!");
            }
        }
        if (!migrations.getType().equals("3")) {
            migrationTables.stream().filter(e -> !e.getMigration() && e.getRandom() != null).forEach(e -> {
                if (e.getMigrationFields().stream().noneMatch(v -> migrations.getType().equals(v.getStatus().toString()))) {
                    throw new ServiceException(e.getName() + "表未找到" + (migrations.getType().equals("1") ? "主键id" : "指定时间"));
                }
            });
        }
    }

    private void verify(Migrations migrations) {
        List<MigrationDataSources> migrationDataSources = migrations.getMigrationDataSources();

        Set<String> uniqueNames = migrationDataSources.stream().map(MigrationDataSources::getUniqueName).collect(Collectors.toSet());
        List<Long> sourcesIds = migrationDataSources.stream()
                .filter(e -> e.getType().equals(1))
                .map(MigrationDataSources::getId).collect(Collectors.toList());
        int count = count(Wrappers.<Migrations>lambdaQuery().eq(Migrations::getName, migrations.getName())
                .ne(migrations.getId() != null, Migrations::getId, migrations.getId()));
        if (count > 0) {
            throw new ServiceException("名称不能重复！");
        }
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
            if (!StringUtil.isEmpty(e.getValue())) {
                e.setValue(StringUtil.upperCase(StringUtil.lineToHump(e.getValue())));
            }
            if (StringUtil.isBlank(e.getRemark())) {
                e.setRemark(e.getPropertyName());
            }
        });
    }

}
