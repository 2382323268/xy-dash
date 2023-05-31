package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xy.dash.entity.DataSources;
import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.mapper.DataSourcesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.DataSourcesService;
import com.xy.dash.service.MigrationDataSourcesService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.Condition;
import com.xy.dash.utli.ObjectUtils;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.api.R;
import com.xy.dash.utli.exception.ServiceException;
import com.xy.dash.vo.AdminMenuVO;
import com.xy.dash.vo.DataSourcesReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据源表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DataSourcesServiceImpl extends ServiceImpl<DataSourcesMapper, DataSources> implements DataSourcesService {

    @Autowired
    private MigrationDataSourcesService migrationDataSourcesService;

    @Override
    public IPage<DataSources> queryPage(Map<String, Object> param, Query query) {
        QueryWrapper<DataSources> queryWrapper = Condition.getQueryWrapper(param, DataSources.class);
        return page(Condition.getPage(query), queryWrapper);
    }

    @Override
    public DataSources detail(Long id) {
        return getById(id);
    }

    @Override
    public Boolean save(DataSourcesReq dataSourcesReq) {
        verify(dataSourcesReq);
        return save(BeanUtil.copyProperties(dataSourcesReq, DataSources.class));
    }

    @Override
    public Boolean deleteLogic(List<String> ids) {
        return removeByIds(ids);
    }

    @Override
    public Boolean updateById(DataSourcesReq dataSourcesReq) {
        verify(dataSourcesReq);
        return updateById(BeanUtil.copyProperties(dataSourcesReq, DataSources.class));
    }

    @Override
    public Boolean deleteByids(List<String> ids) {
        int count = migrationDataSourcesService.count(Wrappers.<MigrationDataSources>lambdaQuery().eq(MigrationDataSources::getSourceId, ids));
        if(count > 0){
            throw new ServiceException("该数据源配置与数据源迁移配置关联，无法删除！");
        }
        return deleteLogic(ids);
    }

    private void verify(DataSourcesReq dataSourcesReq) {
        int count = count(Wrappers.<DataSources>lambdaQuery()
                .ne(!ObjectUtils.isEmpty(dataSourcesReq.getId()), DataSources::getId, dataSourcesReq.getId())
                .eq(DataSources::getName, dataSourcesReq.getName()));
        if (count > 0) {
            throw new ServiceException("数据源名称不能重复！");
        }
    }
}
