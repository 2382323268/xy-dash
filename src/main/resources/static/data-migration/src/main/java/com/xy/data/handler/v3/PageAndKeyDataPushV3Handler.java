package com.xy.data.handler.v3;

import com.xy.data.handler.v2.PageAndKeyDataPushHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: xiangwei
 * @Date: 2022/10/31 15:15
 * @Description 分页+主键id迁移数据
 **/
@Slf4j
public abstract class PageAndKeyDataPushV3Handler<T, R> extends PageAndKeyDataPushHandler<T, R> {
    public PageAndKeyDataPushV3Handler(Class<T> tClass, Class<R> rClass, Class<?> mapper, String start, String end) {
        super(tClass, rClass, mapper, start, end);
    }

    @Override
    protected final void block() {
        await();
    }

    @Override
    protected final void block(Integer size, Integer number) {
        await();
    }

    @Override
    protected final void blockOperation() {
        await();
    }
}
