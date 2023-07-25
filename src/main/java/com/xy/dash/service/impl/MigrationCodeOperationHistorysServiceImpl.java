package com.xy.dash.service.impl;

import com.xy.dash.entity.MigrationCodeOperationHistorys;
import com.xy.dash.mapper.MigrationCodeOperationHistorysMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.MigrationCodeOperationHistorysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 代码运行记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MigrationCodeOperationHistorysServiceImpl extends ServiceImpl<MigrationCodeOperationHistorysMapper, MigrationCodeOperationHistorys> implements MigrationCodeOperationHistorysService {

}
