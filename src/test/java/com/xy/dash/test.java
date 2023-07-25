package com.xy.dash;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.entity.MigrationFields;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.entity.Migrations;
import com.xy.dash.service.MigrationFieldsService;
import com.xy.dash.service.MigrationTablesService;
import com.xy.dash.service.MigrationsService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/11/17 18:25
 * @Description
 **/
@SpringBootTest
@Slf4j
public class test {
    @Autowired
    private MigrationsService migrationsService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void t() {
        /**
         * 1. 映射id迁移
         * 2. 枚举映射
         * 3. 连接查询嵌套
         * 4. 定时任务
         * 5. 支持linux jar运行参数
         * 6. 菜单用户权限登录
         */
        for (int i = 10; i > -1; i--) {
            int finalI = i;
                threadPoolTaskExecutor.execute(()->{
                        int i1 =  1/ 0;
                });


        }
    }



}
