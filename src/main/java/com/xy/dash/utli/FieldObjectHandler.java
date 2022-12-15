package com.xy.dash.utli;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: xiangwei
 * @Date: 2022/4/13 14:29
 * @Description sql 字段处理，修改和新增的时间以及用户
 **/
@Slf4j
@Component
public class FieldObjectHandler implements MetaObjectHandler {
    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createdTime";
    private static final String CREATER = "creater";
    private static final String CREATER_ID = "createrId";
    /**
     * 更新时间
     */
    private static final String MODIFIED_TIME = "modifiedTime";
    private static final String MODIFIED = "modifier";
    private static final String MODIFIED_ID = "modifierId";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...."+ JSON.toJSONString(metaObject));
        this.setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(MODIFIED_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(CREATER, getUser(), metaObject);
        this.setFieldValByName(CREATER_ID, 0L, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName(MODIFIED_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(MODIFIED, getUser(), metaObject);
        this.setFieldValByName(MODIFIED_ID, 0L, metaObject);
    }

    /**
     * 获取当前操作的用户
     *
     * @return UserTDto
     */
    private String getUser() {
        return "0";
    }
}
