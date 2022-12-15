package com.xy.dash.converts;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.xy.dash.enums.DbColumnType;

/**
 * @Author: xiangwei
 * @Date: 2022/11/28 19:57
 * @Description
 **/
public class SqlServerTypeConvert {

    public static DbColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toLowerCase();
        if (!t.contains("char") && !t.contains("xml")) {
            if (t.contains("bigint")) {
                return DbColumnType.LONG;
            } else if (t.contains("int")) {
                return DbColumnType.INTEGER;
            } else if (!t.contains("date") && !t.contains("time")) {
                if (t.contains("text")) {
                    return DbColumnType.STRING;
                } else if (t.contains("bit")) {
                    return DbColumnType.BOOLEAN;
                } else if (!t.contains("decimal") && !t.contains("numeric")) {
                    if (t.contains("money")) {
                        return DbColumnType.BIG_DECIMAL;
                    } else if (!t.contains("binary") && !t.contains("image")) {
                        return !t.contains("float") && !t.contains("real") ? DbColumnType.STRING : DbColumnType.FLOAT;
                    } else {
                        return DbColumnType.BYTE_ARRAY;
                    }
                } else {
                    return DbColumnType.DOUBLE;
                }
            } else {
                byte var5;

                var5 = -1;
                switch (t.hashCode()) {
                    case 3076014:
                        if (t.equals("date")) {
                            var5 = 0;
                        }
                        break;
                    case 3560141:
                        if (t.equals("time")) {
                            var5 = 1;
                        }
                }

                switch (var5) {
                    case 0:
                        return DbColumnType.LOCAL_DATE;
                    case 1:
                        return DbColumnType.LOCAL_TIME;
                    default:
                        return DbColumnType.LOCAL_DATE_TIME;
                }

            }
        } else {
            return DbColumnType.STRING;
        }
    }
}

