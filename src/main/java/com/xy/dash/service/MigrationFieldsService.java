package com.xy.dash.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.entity.MigrationFields;
import com.xy.dash.vo.TemplatesFields;

import java.util.List;

/**
 * <p>
 * 字段配置表 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-29
 */
public interface MigrationFieldsService extends IService<MigrationFields> {

    /**
     * 根据表id获取字段模板
     *
     * @param tableId
     * @return
     */
    List<TemplatesFields> getTemplatesFields(Long tableId, List<String> tableNames);
}
