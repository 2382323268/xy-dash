package com.xy.dash.converts;

import com.xy.dash.enums.DbColumnType;

/**
 * @Author: xiangwei
 * @Date: 2022/12/1 17:49
 * @Description
 **/
public class DataSourceConvert {

    public static DbColumnType getDbColumnType(Integer type, String fieldType) {
        switch (type) {
            case 0:
                return MySqlTypeConvert.processTypeConvert(fieldType);
            case 1:
                return SqlServerTypeConvert.processTypeConvert(fieldType);
            default:
                return null;
        }
    }
}
