package com.xy.dash;

import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.entity.MigrationFields;
import com.xy.dash.entity.MigrationTables;
import com.xy.dash.entity.Migrations;
import com.xy.dash.service.MigrationFieldsService;
import com.xy.dash.service.MigrationTablesService;
import com.xy.dash.service.MigrationsService;
import com.xy.dash.utli.BeanUtil;
import com.xy.dash.utli.SpringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/11/17 18:25
 * @Description
 **/
@SpringBootTest
public class test {
    @Autowired
    private MigrationsService migrationsService;

    @Test
    public void t() {
        MigrationTablesService bean = SpringUtil.getBean(MigrationTablesService.class);
        MigrationTables byId = bean.getById(355);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("key", "value");
        stringStringHashMap.put("key1", "value");
        stringStringHashMap.put("key2", "value");
        byId.setQueryMap(stringStringHashMap);
        bean.updateById(byId);
//        List<Migrations> migrations = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            migrations.add(Migrations.builder().id((long) i).count(i % 1000).build());
//        }
//        long currentTimeMillis1 = System.currentTimeMillis();
//
//        Map<Integer, List<Migrations>> map = migrations.stream().collect(Collectors.groupingBy(Migrations::getCount));
//        for (int i = 1; i < 100; i++) {
//            List<Migrations> collect = map.get(i);
////            System.out.println(collect.stream().map(Migrations::getId).collect(Collectors.toList()));
//        }
//        System.out.println("groupingBy = " + (System.currentTimeMillis() - currentTimeMillis1));
//        long currentTimeMillis = System.currentTimeMillis();
//        for (int i = 1; i < 100; i++) {
//            int finalI = i;
//            List<Migrations> collect = migrations.stream().filter(e -> e.getCount() == finalI).collect(Collectors.toList());
////            System.out.println(collect.stream().map(Migrations::getId).collect(Collectors.toList()));
//        }
//        System.out.println("filter = " + (System.currentTimeMillis() - currentTimeMillis));


    }
}
