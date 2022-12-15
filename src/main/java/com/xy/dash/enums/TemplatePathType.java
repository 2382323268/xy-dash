package com.xy.dash.enums;

import com.xy.dash.vo.TemplatesTables;
import lombok.Getter;

/**
 * @Author: xiangwei
 * @Date: 2022/11/23 10:18
 * @Description
 **/
@Getter
public enum TemplatePathType {
    /**
     * 配置
     */
    APPLICATION("vms/application-dev.yml.vm", "/src/main/resources/application-dev.yml", "config"),
    /**
     * 配置
     */
    DISTRIBUTE("vms/distributeTransactionService.java.vm", "/src/main/java/com/xy/data/util/DistributeTransactionService.java", "config"),
    /**
     * 实体
     */
    ENTITY("vms/entity.java.vm", "/src/main/java/com/xy/data/entity/%s/", "base"),
    /**
     * 接口
     */
    SERVICE("vms/iservice.java.vm", "/src/main/java/com/xy/data/service/%s/", "base"),
    /**
     * 实现类
     */
    IMPL("vms/serviceImpl.java.vm", "/src/main/java/com/xy/data/service/%s/impl/", "base"),
    /**
     * mapper
     */
    MAPPER("vms/mapper.java.vm", "/src/main/java/com/xy/data/mapper/%s/", "base"),
    /**
     * 处理类
     */
    HANDLER("vms/handler.java.vm", "/src/main/java/com/xy/data/handler/", "handler");

    private final String key;
    private final String value;
    private final String type;

    TemplatePathType(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public  boolean isBase() {
       return type.equals("base");
    }

    public  boolean isConfig() {
        return type.equals("config");
    }

    public  boolean isHandler() {
        return type.equals("handler");
    }
}
