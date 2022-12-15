package com.xy.data;

import com.xy.data.handler.DataPushHandler;
import com.xy.data.util.DistributeTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: xiangwei
 * @Date: 2022/10/18 18:12
 * @Description
 **/
@SpringBootTest

public class test {
    @Autowired
    private DistributeTransactionService distributeTransactionService;

    @Test
    public void test1()  {
        /**
         * 1. 测试uuid和雪花算法的速度
         * 2. 模板生成
         * 2. 数据表构思
         * 3. 前端页面
         * 4. setget
         */
        System.out.println(Integer.MAX_VALUE);
    DataPushHandler.run();
    }
}
