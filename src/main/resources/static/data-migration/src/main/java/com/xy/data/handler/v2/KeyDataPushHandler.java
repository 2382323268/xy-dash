package com.xy.data.handler.v2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.data.handler.core.DataPushHandler;
import com.xy.data.util.DataPushConstant;
import com.xy.data.util.SqlConstant;
import com.xy.data.vo.DataCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/10/31 15:54
 * @Description 根据主键id迁移数据
 **/
@Slf4j
public abstract class KeyDataPushHandler<T, R> extends DataPushHandler<T, R> {

    public KeyDataPushHandler(Class<T> tClass, Class<R> rClass, Class<?> mapper, String start, String end) {
        super(tClass, rClass, mapper, start, end);
    }

    @Override
    protected final void pushData(String msg, LocalDateTime start, LocalDateTime end) throws Exception {
        boolean poll = true;
        int index = 0;
        int saveAll = 0;
        int deleteAll = 0;
        String id = null;
        log.info("开始迁移数据: " + msg);

        while (poll) {
            // 1.查数据
            List<R> selectData = selectData(id, start, end);

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
            // 拿出最大的id 下轮筛选大于这个id的数据
            id = id(selectData);

            index++;
            saveAll += dataCountVO.getSave();
            deleteAll += dataCountVO.getDelete();
            log.info("迁移数据 " + msg + ", 第{}轮, 结束id = {}", index, id);
        }
    }

    private List<R> selectData(String id, LocalDateTime start, LocalDateTime end) {
        String idName = getId();
        QueryWrapper<R> queryWrapper = null;
        String createdTime = getCreatedTime();
        if (createdTime == null) {
            queryWrapper = queryWrapper().gt(!StringUtils.isEmpty(id), idName, id);
        } else {
            boolean timestamp = !getFCreatedTime().getType().equals(LocalDateTime.class);
            queryWrapper = queryWrapper()
                    .ge(start != null, createdTime, timestamp ? asLong(start) : start)
                    .le(end != null, createdTime, timestamp ? asLong(end) : end)
                    .gt(!StringUtils.isEmpty(id), idName, id);
        }

        if (SqlConstant.sqlSelect.get(rClass) != null) {
            queryWrapper.select(String.format(DataPushConstant.SQL_SERVER, limit) + SqlConstant.sqlSelect.get(rClass));
        } else {
            queryWrapper.last(DataPushConstant.MYSQL + limit);
        }
        queryWrapper.orderByAsc(idName);
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
