//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.data.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

public class SqlKeyword {
    private static final String SQL_REGEX = "'|%|--|insert|delete|select|count|group|union|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|sql";
    private static final String EQUAL = "_equal";
    private static final String NOT_EQUAL = "_notequal";
    private static final String LIKE = "_like";
    private static final String LIKE_LEFT = "_likeleft";
    private static final String LIKE_RIGHT = "_likeright";
    private static final String NOT_LIKE = "_notlike";
    private static final String GE = "_ge";
    private static final String LE = "_le";
    private static final String GT = "_gt";
    private static final String LT = "_lt";
    private static final String DATE_GE = "_datege";
    private static final String DATE_GT = "_dategt";
    private static final String DATE_EQUAL = "_dateequal";
    private static final String DATE_LT = "_datelt";
    private static final String DATE_LE = "_datele";
    private static final String IS_NULL = "_null";
    private static final String NOT_NULL = "_notnull";
    private static final String IGNORE = "_ignore";

    public SqlKeyword() {
    }

    public static void buildCondition(Map<String, Object> query, QueryWrapper<?> qw) {
        if (!ObjectUtils.isEmpty(query)) {
            query.forEach((k, v) -> {
                if (!hasEmpty(new Object[]{k, v}) && !k.endsWith("_ignore")) {
                    if (k.endsWith("_&eq")) {
                        qw.eq(getColumn(k, "_&eq"), v);
                    } else if (k.endsWith("_&ne")) {
                        qw.ne(getColumn(k, "_&ne"), v);
                    } else if (k.endsWith("_&likeLeft")) {
                        qw.likeLeft(getColumn(k, "_&likeLeft"), v);
                    } else if (k.endsWith("_&likeRight")) {
                        qw.likeRight(getColumn(k, "_&likeRight"), v);
                    } else if (k.endsWith("_&notLike")) {
                        qw.notLike(getColumn(k, "_&notLike"), v);
                    } else if (k.endsWith("_&ge")) {
                        qw.ge(getColumn(k, "_&ge"), v);
                    } else if (k.endsWith("_&le")) {
                        qw.le(getColumn(k, "_&le"), v);
                    } else if (k.endsWith("_&gt")) {
                        qw.gt(getColumn(k, "_&gt"), v);
                    } else if (k.endsWith("_&lt")) {
                        qw.lt(getColumn(k, "_&lt"), v);
                    } else if (k.endsWith("_&isNull")) {
                        qw.isNull(getColumn(k, "_&isNull"));
                    } else if (k.endsWith("_&isNotNull")) {
                        qw.isNotNull(getColumn(k, "_&isNotNull"));
                    } else if (k.endsWith("_&like")) {
                        qw.like(getColumn(k, "_&like"), v);
                    } else if (k.endsWith("_&in")) {
                        qw.in(getColumn(k, "_&in"), (Collection) v);
                    } else if (k.endsWith("_&notIn")) {
                        qw.notIn(getColumn(k, "_&notIn"), (Collection) v);
                    } else if (k.endsWith("_&last")) {
                        qw.last(v.toString());
                    }

                }
            });
        }
    }

    private static String getColumn(String column, String keyword) {
        return humpToUnderline(removeSuffix(column, keyword));
    }

    public static String filter(String param) {
        return param == null ? null : param.replaceAll("(?i)'|%|--|insert|delete|select|count|group|union|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|sql", "");
    }

    public static boolean hasEmpty(Object... os) {
        Object[] var1 = os;
        int var2 = os.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Object o = var1[var3];
            if (ObjectUtils.isEmpty(o)) {
                return true;
            }
        }

        return false;
    }

    public static String lowerFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] = (char) (arr[0] + 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static String humpToUnderline(String para) {
        para = lowerFirst(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;

        for (int i = 0; i < para.length(); ++i) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                ++temp;
            }
        }

        return sb.toString().toLowerCase();
    }

    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(suffix)) {
            String str2 = str.toString();
            return str2.endsWith(suffix.toString()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        } else {
            return "";
        }
    }

    public static String subPre(CharSequence string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (StringUtils.isEmpty(str)) {
            return "";
        } else {
            int len = str.length();
            if (fromIndex < 0) {
                fromIndex += len;
                if (fromIndex < 0) {
                    fromIndex = 0;
                }
            } else if (fromIndex > len) {
                fromIndex = len;
            }

            if (toIndex < 0) {
                toIndex += len;
                if (toIndex < 0) {
                    toIndex = len;
                }
            } else if (toIndex > len) {
                toIndex = len;
            }

            if (toIndex < fromIndex) {
                int tmp = fromIndex;
                fromIndex = toIndex;
                toIndex = tmp;
            }

            return fromIndex == toIndex ? "" : str.toString().substring(fromIndex, toIndex);
        }
    }
}
