package com.xy.dash.service;

import com.xy.dash.entity.MigrationCodeOperations;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.vo.MigrationCodeOperationDetails;

/**
 * <p>
 * 代码运行记录表 服务类
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
public interface MigrationCodeOperationsService extends IService<MigrationCodeOperations> {

    void run(Long id, String fileName);

    MigrationCodeOperationDetails details(Long id);
}
