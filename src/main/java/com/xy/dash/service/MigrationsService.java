package com.xy.dash.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.Migrations;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.utli.Query;

import java.util.Map;

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
    void generatingCode(Long id, String remark);

    /**
     * 添加配置
     */
    void add(Migrations migrations);

    /**
     * 删除
     */
    void batchDelete(Long id);

    IPage<Migrations> queryPage(Map<String, Object> param, Query query);

    Migrations details(Long id);
}
