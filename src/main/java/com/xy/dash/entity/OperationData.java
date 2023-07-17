package com.xy.dash.entity;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.dash.utli.SpringUtil;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2023/6/30 14:53
 * @Description 自己的项目 随便玩玩
 **/
public class OperationData<T, V> {

    private Boolean isNull;
    private List<V> ids;
    private Map<V, T> map;
    private Map<V, List<T>> groupMap;

    private final List<T> list;
    private final Function<T,V> field;

    private OperationData(List<T> list, Function<T,V> field) {
        if (CollectionUtils.isEmpty(list)) {
            list = Collections.emptyList();
            isNull = true;
        } else {
            isNull = false;
        }
        this.list = list;
        this.field = field;
    }

    public List<T> getList() {
        return list;
    }

    public List<V> getFields() {
        if (ids == null) {
            this.ids = (List<V>) list.stream().map(field).collect(Collectors.toList());
        }
        return ids;
    }


    public Map<V, T> getMap() {
        if (map == null) {
            this.map = (Map<V, T>) list.stream().collect(Collectors.toMap(field, Function.identity()));
        }
        return map;
    }

    public Map<V, List<T>> getGroupMap() {
        if (groupMap == null) {
            this.groupMap = (Map<V, List<T>>) list.stream().collect(Collectors.groupingBy(field));
        }
        return groupMap;
    }

    public void ifPresent(Consumer<List<T>> consumer) {
        if (isNull) {
            return;
        }
        consumer.accept(list);
    }

    public static <T, V> OperationData<T, V> create(List<T> list, Function<T,V> field) {
        return new OperationData<T, V>(list, field) {
        };
    }

    public static <T, V> OperationData<T, V> create(OperationData<T, ?> operationData, Function<T,V> field) {
        return new OperationData<T, V>(operationData.getList(), field) {
        };
    }

    public static <T, V> OperationData<T, V> create(Class<T> tClass, SFunction<T,V> field) {
        return create(tClass, null, field);
    }

    public static <T, V> OperationData<T, V> create(Class<T> tClass, Object param, SFunction<T,V> field) {
        IService<T> serviceBean = SpringUtil.getServiceBean(tClass);
        List<T> list = null;

        if (param == null) {
            list = serviceBean.list();
        } else if (param instanceof Collection) {
            list = serviceBean.list(Wrappers.<T>lambdaQuery().in(field, (Collection) param));
        } else {
            list = serviceBean.list(Wrappers.<T>lambdaQuery().eq(field, param));
        }
        return new OperationData<T, V>(list, field) {
        };
    }
}
