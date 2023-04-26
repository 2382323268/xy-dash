package com.xy.dash.factory;

import com.xy.dash.utli.exception.NotFoundFactoryException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: xiangwei
 * @Date: 2023/3/10 15:20
 * @Description
 **/
public final class DataBaseMapping {
    private final Map<String, DataBaseHandler> handlers;

    public DataBaseMapping(List<DataBaseHandler> dataBaseList) {
        Map<String, DataBaseHandler> handleMapping = new HashMap<>();
        for (DataBaseHandler dataBase : dataBaseList) {
            handleMapping.put(dataBase.type(), dataBase);
        }
        this.handlers = handleMapping;
    }

    public DataBaseHandler get(String type) {
        return Optional.ofNullable(handlers.get(type)).orElseThrow(NotFoundFactoryException::new);
    }

}
