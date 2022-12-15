package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.dash.entity.DataSources;
import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.mapper.DataSourcesMapper;
import com.xy.dash.mapper.MigrationDataSourcesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.MigrationDataSourcesService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.vo.TemplatesDataSources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据迁移数据源配置 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MigrationDataSourcesServiceImpl extends ServiceImpl<MigrationDataSourcesMapper, MigrationDataSources> implements MigrationDataSourcesService {

    @Autowired
    private DataSourcesMapper dataSourcesMapper;

    @Override
    public List<TemplatesDataSources> getTemplatesDataSources(Long migrationsId) {
        List<TemplatesDataSources> templatesDataSources = new ArrayList<>();

        List<MigrationDataSources> migrationDataSourcesList = list(new LambdaQueryWrapper<MigrationDataSources>().eq(MigrationDataSources::getMigrationsId, migrationsId));
        List<Long> ids = migrationDataSourcesList.stream().map(MigrationDataSources::getSourceId).collect(Collectors.toList());
        Map<Long, DataSources> dataSourcesMap = dataSourcesMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(DataSources::getId, Function.identity()));

        migrationDataSourcesList.forEach(e -> {
            TemplatesDataSources templatesDataSource = new TemplatesDataSources();
            DataSources dataSources = dataSourcesMap.get(e.getSourceId());

            BeanUtil.copyProperties(e, templatesDataSource);
            BeanUtil.copyProperties(dataSources, templatesDataSource);
            templatesDataSource.setId(e.getId());
            templatesDataSources.add(templatesDataSource);
        });
        return templatesDataSources;
    }
}
