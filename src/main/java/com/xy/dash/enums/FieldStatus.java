package com.xy.dash.enums;

import com.xy.dash.utli.StringUtil;
import com.xy.dash.utli.exception.ServiceException;
import io.swagger.models.auth.In;
import lombok.Getter;

/**
 * @Author: xiangwei
 * @Date: 2022/11/23 10:18
 * @Description
 **/
@Getter
public enum FieldStatus {
    /**
     * 普通
     */
    NORMAL(0, null),
    /**
     * 主键
     */
    KEY(1, "Id"),
    /**
     * 创建
     */
    CREATED(2, "CreatedTime");

    private final Integer code;
    private final String name;

    FieldStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getPropertyName(String propertyName, Integer status) {
        for (FieldStatus value : values()) {
            if (value.getCode().equals(status)) {
                return StringUtil.isEmpty(value.getName()) ? propertyName : value.getName();
            }
        }
        throw new ServiceException("未找到对应的字段状态, status = " + status);
    }

}
