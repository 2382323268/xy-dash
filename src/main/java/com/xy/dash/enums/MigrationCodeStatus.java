package com.xy.dash.enums;

import lombok.Getter;

/**
 * @Author: xiangwei
 * @Date: 2022/11/23 10:18
 * @Description
 **/
@Getter
public enum MigrationCodeStatus {
    /**
     * 运行失败
     */
    FAIL(0, "运行失败"),

    /**
     * 运行成功
     */
    SUCCEES(1, "运行成功"),

    /**
     * 开始执行
     */
    START(2, "开始执行"),

    /**
     * 生成jar
     */
    CREATE_JAR(3, "生成jar"),

    /**
     * 运行中
     */
    RUN(4, "运行中");


    private final Integer code;
    private final String value;

    MigrationCodeStatus(Integer code, String value) {
        this.value = value;
        this.code = code;
    }

    public static String valueByCode(Integer code) {
        for (MigrationCodeStatus value : values()) {
            if (value.code.equals(code)) {
                return value.value;
            }
        }
        return null;
    }
}
