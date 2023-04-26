package com.xy.dash;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
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
        MigrationsService bean = SpringUtil.getBean(MigrationsService.class);
        bean.generatingCode(116L);
//        Migrations migrations = new Migrations();
//        migrations.setPort(9008);
//        migrations.setStartThread(true);
//        migrations.setCount(20000);
//        migrations.setThreadCount(50);
//        migrations.setSqlSpliec(true);
//        List<MigrationDataSources> migrationDataSources = new ArrayList<>();
//        List<MigrationTables> migrationTables = new ArrayList<>();
//        List<MigrationTables> migrationTables2 = new ArrayList<>();
//        MigrationDataSources migrationDataSources1 = new MigrationDataSources();
//        MigrationDataSources migrationDataSources2 = new MigrationDataSources();
//        MigrationTables migrationTable = new MigrationTables();
//        MigrationTables migrationTable2 = new MigrationTables();
//        migrationDataSources1.setSourceId(1L);
//        migrationDataSources1.setDatabaseName("yuexiuhui");
//        migrationDataSources1.setType(0);
//        migrationDataSources1.setUniqueName("wxw");
//        migrationDataSources2.setSourceId(1L);
//        migrationDataSources2.setDatabaseName("yuexiuhui_test");
//        migrationDataSources2.setType(0);
//        migrationDataSources2.setUniqueName("wxw1");
//        migrationTable.setMigration(true);
//        migrationTable.setName("yxh_user_recommend_records");
//        migrationTable.setPosition(0);
//        migrationTable2.setMigration(false);
//        migrationTable2.setName("yxh_user_recommend_records");
//        migrationTable2.setPosition(0);
//        List<MigrationFields> migrationFieldsList = new ArrayList<>();
//        List<MigrationFields> migrationFieldsList2 = new ArrayList<>();
//        MigrationFields migrationFields1 = new MigrationFields();
//        MigrationFields migrationFields2 = new MigrationFields();
//        migrationFields1.setFieldName("id");
//        migrationFields1.setFieldType("varchar");
//        migrationFields2.setFieldName("card_no");
//        migrationFields2.setFieldType("varchar");
//
//        migrationFieldsList.add(migrationFields1);
//        migrationFieldsList.add(migrationFields2);
//        migrationFieldsList2.add(migrationFields1);
//        migrationFieldsList2.add(migrationFields2);
//        migrationTable.setMigrationFields(migrationFieldsList);
//        migrationTable2.setMigrationFields(migrationFieldsList2);
//        migrationTables.add(migrationTable);
//        migrationTables2.add(migrationTable2);
//        migrationDataSources1.setMigrationTables(migrationTables);
//        migrationDataSources2.setMigrationTables(migrationTables2);
//        migrationDataSources.add(migrationDataSources1);
//        migrationDataSources.add(migrationDataSources2);
//        migrations.setMigrationDataSources(migrationDataSources);
//        bean.add(migrations);
    }
}
