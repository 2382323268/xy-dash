package com.xy.dash.service;

import com.xy.dash.entity.MigrationDataSources;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.vo.TemplatesDataSources;

import java.util.List;

/**
 * <p>
 * 数据迁移数据源配置 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface MigrationDataSourcesService extends IService<MigrationDataSources>{

    /**
     * 根据迁移配置id 生成 数据源配置模板
     * @param migrationsId
     * @return
     */
    List<TemplatesDataSources> getTemplatesDataSources(Long migrationsId);
}
