package com.xy.dash.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.DataSources;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.entity.Migrations;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.api.R;
import com.xy.dash.vo.AdminMenuVO;
import com.xy.dash.vo.DataSourcesReq;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据源表 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface DataSourcesService extends IService<DataSources> {

    IPage<DataSources> queryPage(Map<String, Object> param, Query query);

    DataSources detail(Long id);

    Boolean save(DataSourcesReq dataSourcesReq);

    Boolean deleteLogic(List<String> ids);

    Boolean updateById(DataSourcesReq dataSourcesReq);

    Boolean deleteByids(List<String> asList);
}
