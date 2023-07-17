package com.xy.dash;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.xy.dash.controller.MigrationsController;
import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.entity.MigrationFields;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.entity.Migrations;
import com.xy.dash.mapper.MigrationsMapper;
import com.xy.dash.service.MigrationsService;
import com.xy.dash.service.TemplatesService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.SpringUtil;
import com.xy.dash.vo.TemplatesAdd;
import com.xy.dash.vo.TemplatesDataSources;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: xiangwei
 * @Date: 2022/11/14 15:26
 * @Description
 **/
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan(basePackages = "com.xy.dash.mapper")
public class XyDashApplication {

    public static void main(String[] args) {
        SpringApplication.run(XyDashApplication.class, args);
//        MigrationsService bean = SpringUtil.getBean(MigrationsService.class);
//        bean.generatingCode(180L);
    }
}
