package com.xy.dash.enums;

import lombok.Getter;

/**
 * @Author: xiangwei
 * @Date: 2022/11/23 10:18
 * @Description
 **/
@Getter
public enum MenuType {
    /**
     * 目录
     */
    M("M", "目录"),
    /**
     * 菜单
     */
    C("C", "菜单"),
    /**
     * 按钮
     */
    F("F", "按钮");

    private final String value;
    private final String type;

    MenuType(String value, String type) {
        this.value = value;
        this.type = type;
    }

}
