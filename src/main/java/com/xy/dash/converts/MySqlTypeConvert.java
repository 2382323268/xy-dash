package com.xy.dash.converts;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.xy.dash.enums.DbColumnType;

/**
 * @Author: xiangwei
 * @Date: 2022/11/28 19:21
 * @Description
 **/
public class MySqlTypeConvert{

    public static DbColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return DbColumnType.STRING;
        } else if (t.contains("bigint")) {
            return DbColumnType.LONG;
        } else if (t.contains("tinyint(1)")) {
            return DbColumnType.BOOLEAN;
        } else if (t.contains("int")) {
            return DbColumnType.INTEGER;
        } else if (t.contains("text")) {
            return DbColumnType.STRING;
        } else if (t.contains("bit")) {
            return DbColumnType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return DbColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return DbColumnType.CLOB;
        } else if (t.contains("blob")) {
            return DbColumnType.BLOB;
        } else if (t.contains("binary")) {
            return DbColumnType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return DbColumnType.FLOAT;
        } else if (t.contains("double")) {
            return DbColumnType.DOUBLE;
        } else if (!t.contains("json") && !t.contains("enum")) {
            if (t.contains("date") || t.contains("time") || t.contains("year")) {
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
                        break;
                    case 3704893:
                        if (t.equals("year")) {
                            var5 = 2;
                        }
                }

                switch (var5) {
                    case 0:
                        return DbColumnType.LOCAL_DATE;
                    case 1:
                        return DbColumnType.LOCAL_TIME;
                    case 2:
                        return DbColumnType.YEAR;
                    default:
                        return DbColumnType.LOCAL_DATE_TIME;
                }
            }

            return DbColumnType.STRING;
        } else {
            return DbColumnType.STRING;
        }
    }
}

