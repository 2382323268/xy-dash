package com.xy.dash;

import com.xy.dash.entity.MigrationDataSources;
import com.xy.dash.entity.Migrations;
import com.xy.dash.service.MigrationsService;
import com.xy.dash.utli.SpringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void  t(){
        MigrationsService bean = SpringUtil.getBean(MigrationsService.class);
        bean.generatingCode(6L);
    }
}
