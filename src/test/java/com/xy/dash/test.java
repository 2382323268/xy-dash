package com.xy.dash;

import com.xy.dash.entity.Migrations;
import com.xy.dash.service.MigrationsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Migrations migrations = new Migrations();
        migrations.setCount(20000);
        migrations.setStartThread(true);
        migrationsService.save(migrations);
    }
}
