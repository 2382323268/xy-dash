package com.xy.data;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.xy.data.handler.DataPushHandler;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: xiangwei
 * @Date: 2022/10/18 17:57
 * @Description
 **/
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan(basePackages = "com.xy.data.mapper")
@Slf4j
public class DataMigrationApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DataMigrationApplication.class, args);
        DataPushHandler.run();
        run.close();
        log.info("已停止服务器！");
    }
}
