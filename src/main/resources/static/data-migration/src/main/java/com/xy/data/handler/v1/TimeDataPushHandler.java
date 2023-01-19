package com.xy.data.handler.v1;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.data.handler.core.DataPushHandler;
import com.xy.data.util.DataPushConstant;
import com.xy.data.util.SqlConstant;
import com.xy.data.vo.DataCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/10/31 15:54
 * @Description 根据创建时间迁移数据
 **/
@Slf4j
public abstract class TimeDataPushHandler<T, R> extends DataPushHandler<T, R> {

    public TimeDataPushHandler(Class<T> tClass, Class<R> rClass, Class<?> mapper, String start, String end) {
        super(tClass, rClass, mapper, start, end);
    }

    @Override
    protected final void pushData(String msg, LocalDateTime start, LocalDateTime end) throws Exception {
        boolean poll = true;
        int index = 0;
        int saveAll = 0;
        int deleteAll = 0;
        List<String> neIds = null;
        start = start == null ? LocalDateTime.of(1, 1, 1, 0, 0) : start;

        log.info("开始迁移数据: " + msg + ", 开始时间: {}", df.format(start));

        while (poll) {
            // 1.查数据
            List<R> selectData = selectData(neIds, start, end);

            if (selectData == null || selectData.size() == 0) {
                poll = false;
                log.info("迁移数据" + msg + "完成, 一共添加{}条记录, 删除重复数据{}条", saveAll, deleteAll);
                return;
            }
            // 单线程或者多线程
            DataCountVO dataCountVO = singleOrMultiThread(selectData, (e, number) -> {
                /**
                 * 2.数据转换
                 */
                List<T> data = getData(e);
                /**
                 * 3.删除重复数据
                 */
                // 删除之前逻辑扩展
                deleteBeforeExtension(e, data);

                int delete = deleteByIds(data);

                /**
                 * 1.多线程需要等所有线程删除完再走下一步
                 * 2.删除+索引失效 会表锁
                 * 3.导致事务死锁
                 */
                block(selectData.size(), number);
                /**
                 * 4.保存数据
                 */
                // 保存之前逻辑扩展
                saveBeforeExtension(e, data);

                int save = saveAll(data);
                // 保存之后逻辑扩展
                saveAfterExtension(e, data);

                return DataCountVO.builder().save(save).delete(delete).build();
            });
            count.set(0);
            start = startDateTime(selectData);
            /**
             * 1. 拿出 结束时间(下次轮询的开始时间 ==  startDateTime)  相等的ids
             * 2. 下一轮查询 排除掉这些ids 防止死循环
             */
            neIds = neIds(start, selectData);
            index++;
            saveAll += dataCountVO.getSave();
            deleteAll += dataCountVO.getDelete();
            log.info("迁移数据 " + msg + ", 第{}轮, 结束时间 start = {}", index, df.format(start));
        }
    }

    private List<R> selectData(List<String> neIds, LocalDateTime start, LocalDateTime end) {
        String id = getId();
        String createdTime = getCreatedTime();
        boolean timestamp = !getFCreatedTime().getType().equals(LocalDateTime.class);
        QueryWrapper<R> queryWrapper = queryWrapper()
                .ge(createdTime, timestamp ? asLong(start) : start)
                .le(end != null, createdTime, timestamp ? asLong(end) : end)
                .notIn(!CollectionUtils.isEmpty(neIds), id, neIds);
        if (SqlConstant.sqlSelect.get(rClass) != null) {
            queryWrapper.select(String.format(DataPushConstant.SQL_SERVER, limit) + SqlConstant.sqlSelect.get(rClass));
        } else {
            queryWrapper.last(DataPushConstant.MYSQL + limit);
        }
        queryWrapper.orderByAsc(createdTime);
        return getRService().list(queryWrapper);
    }

    /**
     * 单线程或者多线程执行
     *
     * @param selectData
     * @return
     * @throws Exception
     */
    private DataCountVO singleOrMultiThread(List<R> selectData, BiFunction<List<R>, Integer, DataCountVO> function) throws Exception {

        int count = limit / threadCount;
        //单线程处理
        if (!isThread) {
            // 具体的逻辑处理 抽象出去
            return function.apply(selectData, count);
        }

        int save = 0;
        int delete = 0;

        //多线程处理
        List<Future<DataCountVO>> list = new ArrayList<>();
        for (int i = 0; i < selectData.size(); i = i + count) {
            int finalI = i;
            list.add(pushExecutor.submit(() -> {
                List<R> collect = selectData.stream().skip(finalI).limit(count).collect(Collectors.toList());
                try {
                    // 具体的逻辑处理 抽象出去
                    return function.apply(collect, count);
                } catch (Exception e) {
                    return DataCountVO.builder().throwable(e).build();
                }
            }));
        }
        for (Future<DataCountVO> integerFuture : list) {
            DataCountVO dataCountVO = integerFuture.get();
            throwException(dataCountVO);
            save += dataCountVO.getSave();
            delete += dataCountVO.getDelete();
        }
        return DataCountVO.builder().save(save).delete(delete).build();
    }
}
