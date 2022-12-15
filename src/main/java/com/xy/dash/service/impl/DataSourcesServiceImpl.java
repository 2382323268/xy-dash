package com.xy.dash.service.impl;

import com.xy.dash.entity.DataSources;
import com.xy.dash.mapper.DataSourcesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.DataSourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
