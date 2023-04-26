package com.xy.dash.enums;

import lombok.Getter;

/**
 * @Author: xiangwei
 * @Date: 2023/3/10 16:11
 * @Description
 **/
@Getter
public enum DataSourceType {
    /**
     * 目录
     */
    MYSQL(0, "mysql"),
    /**
     * 菜单
     */
    SQL_SERVER(1, "sqlserver");


    private final Integer type;
    private final String value;

    DataSourceType(Integer type, String value) {
        this.value = value;
        this.type = type;
    }

    public static String valueByType(Integer type) {
        for (DataSourceType value : values()) {
            if (value.type.equals(type)) {
                return value.value;
            }
        }
        return null;
    }
}
