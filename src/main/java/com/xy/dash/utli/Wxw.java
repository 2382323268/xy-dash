package com.xy.dash.utli;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @Author: xiangwei
 * @Date: 2023/8/7 17:21
 * @Description
 **/
public class Wxw implements TransactionSynchronization {
    @Override
    public void afterCompletion(int status) {
        if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
            TransactionTemplate bean = SpringUtil.getBean(TransactionTemplate.class);
            bean.execute((v)->{
                JdbcTemplate jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
                String sql = String.format("UPDATE xy_migrations set count = 5004  where id in (1,6,100)");
                // 更新 失败日志
                jdbcTemplate.update(sql);
                return null;
            });


        }
    }
}
