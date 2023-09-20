package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.dash.entity.*;
import com.xy.dash.enums.MigrationCodeStatus;
import com.xy.dash.mapper.MigrationCodesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.*;
import com.xy.dash.utli.Condition;
import com.xy.dash.utli.ObjectUtils;
import com.xy.dash.utli.Query;
import com.xy.dash.utli.XyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 代码记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MigrationCodesServiceImpl extends ServiceImpl<MigrationCodesMapper, MigrationCodes> implements MigrationCodesService {

    @Autowired
    private TemplatesService templatesService;

    @Autowired
    private MigrationCodeOperationsService migrationCodeOperationsService;

    @Autowired
    private MigrationCodeOperationHistorysService migrationCodeOperationHistorysService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public IPage<MigrationCodes> queryPage(Map<String, Object> param, Query query) {
        QueryWrapper<MigrationCodes> queryWrapper = Condition.getQueryWrapper(param, MigrationCodes.class);
        IPage<MigrationCodes> page = page(Condition.getPage(query), queryWrapper);

        if (page.getTotal() == 0L) {
            return page;
        }

        List<Long> migrationIds = page.getRecords().stream().map(MigrationCodes::getMigrationId).distinct().collect(Collectors.toList());
        OperationData<Migrations, Long> operationData = OperationData.create(Migrations.class, migrationIds, Migrations::getId);
        Map<Long, Migrations> map = operationData.getMap();

        page.getRecords().forEach(e -> {
            Migrations migrations = map.get(e.getMigrationId());
            if (ObjectUtils.isNotEmpty(migrations)) {
                e.setMigrationName(migrations.getName());
            }
        });
        return page;
    }

    @Override
    public void delete(Long id) {
        templatesService.deleteFile(new File(XyConstant.PATH.concat(getById(id).getFileName())));
        removeById(id);
    }

    @Override
    public void run(Long id, String remark) {
        synchronized (this) {
            MigrationCodes migrationCodes = getById(id);
            migrationCodes.setRunCount(migrationCodes.getRunCount() + 1);

            MigrationCodeOperations migrationCodeOperations = MigrationCodeOperations.builder()
                    .codeId(id)
                    .remark(remark)
                    .status(MigrationCodeStatus.START.getCode())
                    .fileName(migrationCodes.getFileName())
                    .build();
            migrationCodeOperationsService.save(migrationCodeOperations);

            MigrationCodeOperationHistorys migrationCodeOperationHistorys = MigrationCodeOperationHistorys.builder()
                    .operationId(migrationCodeOperations.getId())
                    .status(MigrationCodeStatus.START.getCode())
                    .build();
            migrationCodeOperationHistorysService.save(migrationCodeOperationHistorys);
            updateById(migrationCodes);

            threadPoolTaskExecutor.execute(() -> {
                migrationCodeOperationsService.run(migrationCodeOperations.getId(), migrationCodes.getFileName());
            });
        }


    }
}
