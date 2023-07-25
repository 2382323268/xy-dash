package com.xy.dash.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xy.dash.entity.MigrationCodeOperationHistorys;
import com.xy.dash.entity.MigrationCodeOperations;
import com.xy.dash.enums.MigrationCodeStatus;
import com.xy.dash.mapper.MigrationCodeOperationsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.dash.service.MigrationCodeOperationHistorysService;
import com.xy.dash.service.MigrationCodeOperationsService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.ObjectUtils;
import com.xy.dash.utli.StringUtil;
import com.xy.dash.utli.XyConstant;
import com.xy.dash.utli.exception.ServiceException;
import com.xy.dash.vo.MigrationCodeOperationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 代码运行记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-07-17
 */
@Slf4j
@Service
public class MigrationCodeOperationsServiceImpl extends ServiceImpl<MigrationCodeOperationsMapper, MigrationCodeOperations> implements MigrationCodeOperationsService {

    @Autowired
    private MigrationCodeOperationHistorysService migrationCodeOperationHistorysService;

    @Override
    public void run(Long id, String fileName) {
        String os = System.getProperty("os.name");
        String createJar = null;
        String run = null;


        if (os != null && os.toLowerCase().startsWith("windows")) {
            createJar = String.format(XyConstant.CMD_CREATE_JAY, fileName);
            run = String.format(XyConstant.CMD_RUN_JAY, fileName, fileName, id);
        } else {
            createJar = String.format(XyConstant.LINUX_CREATE_JAY, fileName);
            run = String.format(XyConstant.LINUX_RUN_JAY, fileName, fileName, id);
        }

        try {
            executiveOrder(createJar, id, MigrationCodeStatus.CREATE_JAR.getCode());
            executiveOrder(run, id, MigrationCodeStatus.RUN.getCode());
        } catch (Exception e) {
            error(id, e.getMessage());
            return;
        }
        MigrationCodeOperationHistorys migrationCodeOperationHistorys = MigrationCodeOperationHistorys.builder()
                .operationId(id)
                .status(MigrationCodeStatus.SUCCEES.getCode())
                .build();
        migrationCodeOperationHistorysService.save(migrationCodeOperationHistorys);
        update(Wrappers.<MigrationCodeOperations>lambdaUpdate().eq(MigrationCodeOperations::getId, id)
                .set(MigrationCodeOperations::getStatus, MigrationCodeStatus.SUCCEES.getCode()));
    }

    @Override
    public MigrationCodeOperationDetails details(Long id) {
        MigrationCodeOperations migrationCodeOperations = getById(id);
        if (ObjectUtils.isEmpty(migrationCodeOperations)) {
            return new MigrationCodeOperationDetails();
        }
        List<MigrationCodeOperationHistorys> list = migrationCodeOperationHistorysService.list(Wrappers.<MigrationCodeOperationHistorys>lambdaQuery()
                .in(MigrationCodeOperationHistorys::getOperationId, id));
        list.forEach(e -> {
            e.setMsg(MigrationCodeStatus.valueByCode(e.getStatus()).concat(" ").concat(e.getMsg() == null ? "" : "失败原因: " + e.getMsg()));
            if(e.getStatus().equals(MigrationCodeStatus.FAIL.getCode())){
                e.setColor("#ED1C24");
            }else {
                e.setColor("#0bbd87");
            }
        });
        MigrationCodeOperationHistorys last = list.get(list.size() - 1);
        if (!Arrays.asList(MigrationCodeStatus.FAIL.getCode(), MigrationCodeStatus.SUCCEES.getCode()).contains(last.getStatus())) {
            last.setColor("#0096D6");
        }

        MigrationCodeOperationDetails migrationCodeOperationDetails = BeanUtil.copyProperties(migrationCodeOperations, MigrationCodeOperationDetails.class);
        migrationCodeOperationDetails.setMigrationCodeOperationHistorysList(list);
        return migrationCodeOperationDetails;
    }

    public static String getMsg(InputStream stream) throws Exception {
        String line = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "GB2312"));
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private void executiveOrder(String order, Long id, Integer status) throws Exception {

        Runtime runtime = Runtime.getRuntime();
        Process exec = null;
        BufferedReader br = null;
        MigrationCodeOperationHistorys migrationCodeOperationHistorys = MigrationCodeOperationHistorys.builder()
                .operationId(id)
                .status(status)
                .build();
        migrationCodeOperationHistorysService.save(migrationCodeOperationHistorys);
        update(Wrappers.<MigrationCodeOperations>lambdaUpdate().eq(MigrationCodeOperations::getId, id)
                .set(MigrationCodeOperations::getStatus, status));
        try {
            exec = runtime.exec(order);
            String msg = getMsg(exec.getInputStream());
            String error = getMsg(exec.getErrorStream());
            if (StringUtil.isNotBlank(error)) {
                throw new ServiceException(error);
            }
            if (msg.toLowerCase().contains("error")) {
                throw new ServiceException(msg);
            }

        } finally {
            if (exec != null) {
                exec.destroy();
            }
            if (br != null) {
                br.close();
            }
        }
    }

    private void error(Long id, String msg) {
        MigrationCodeOperationHistorys historys = MigrationCodeOperationHistorys.builder()
                .operationId(id)
                .status(MigrationCodeStatus.FAIL.getCode())
                .msg(msg)
                .build();
        migrationCodeOperationHistorysService.save(historys);
        update(Wrappers.<MigrationCodeOperations>lambdaUpdate().eq(MigrationCodeOperations::getId, id)
                .set(MigrationCodeOperations::getStatus, MigrationCodeStatus.FAIL.getCode()));
        throw new ServiceException(msg);
    }
}
