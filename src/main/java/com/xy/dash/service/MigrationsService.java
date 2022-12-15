package com.xy.dash.service;

import com.xy.dash.entity.Migrations;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据迁移配置 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface MigrationsService extends IService<Migrations> {

    /**
     * 生成代码
     */
    void generatingCode(Long id);

    /**
     * 添加配置
     */
    void add(Migrations migrations);
}
