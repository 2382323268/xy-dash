package com.xy.dash.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.MigrationCodes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.utli.Query;

import java.util.Map;

/**
 * <p>
 * 代码记录表 服务类
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
public interface MigrationCodesService extends IService<MigrationCodes> {

    IPage<MigrationCodes> queryPage(Map<String, Object> param, Query query);

    void delete(Long id);

    void run(Long id, String remark);
}
