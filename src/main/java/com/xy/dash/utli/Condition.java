//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.dash.utli;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Condition {
    private static final Set<String> exclude = new HashSet<>();

    public Condition() {
    }

    static {
        exclude.add("current");
        exclude.add("size");
        exclude.add("ascs");
        exclude.add("descs");
    }

    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page((long) toInt(query.getCurrent(), 1), (long) toInt(query.getSize(), 10));
        return page;
    }

    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper(entity);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
        exclude.forEach(e -> {
            query.remove(e);
        });
        QueryWrapper<T> qw = new QueryWrapper();
        qw.setEntity(BeanUtil.newInstance(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }

    public static int toInt(@Nullable final Object value, final int defaultValue) {
        String str = String.valueOf(value);
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Integer.valueOf(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }
}
