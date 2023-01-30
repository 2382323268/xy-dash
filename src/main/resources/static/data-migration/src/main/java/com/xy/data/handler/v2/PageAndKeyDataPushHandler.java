package com.xy.data.handler.v2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.data.handler.core.DataPushHandler;
import com.xy.data.vo.DataCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @Author: xiangwei
 * @Date: 2022/10/31 15:15
 * @Description 分页+主键id迁移数据
 **/
@Slf4j
@Deprecated
public abstract class PageAndKeyDataPushHandler<T, R> extends DataPushHandler<T, R> {
    public PageAndKeyDataPushHandler(Class<T> tClass, Class<R> rClass, Class<?> mapper, String start, String end) {
        super(tClass, rClass, mapper, start, end);
    }

    @Override
    protected final void pushData(String msg, LocalDateTime start, LocalDateTime end) throws Exception {
        AtomicInteger current = new AtomicInteger(0);
        int size = isThread ? limit / threadCount : limit;
        int saveAll = 0;
        int index = 0;
        int deleteAll = 0;
        String id = null;

        int total = total(start, end);

        log.info("开始数据迁移: " + msg + ",  分页总数量: {}", total);

        for (int i = 0; i < total; i = i + limit) {

            // 单线程或者多线程
            LocalDateTime finalStart = start;
            String finalId = id;
            DataCountVO dataCountVO = singleOrMultiThread(() -> {
                // 1.查数据
                List<R> selectData = selectData(current, size, finalStart, end, finalId);
                if (selectData == null || selectData.size() == 0) {
                    blockOperation();
                    return DataCountVO.builder().save(0).delete(0).build();
                }
                /**
                 * 2.数据转换
                 */
                List<T> data = getData(selectData);

                // 删除之前逻辑扩展
                deleteBeforeExtension(selectData, data);
                /**
                 * 3.删除重复数据
                 */
                int delete = deleteByIds(data);
                /**
                 * 1.多线程需要等所有线程删除完再走下一步
                 * 2.删除+索引失效 会表锁
                 * 3.导致事务死锁
                 */
                block();
                // 保存之前逻辑扩展
                saveBeforeExtension(selectData, data);
                /**
                 * 4.保存数据
                 */
                int save = saveAll(data);
                // 保存之后逻辑扩展
                saveAfterExtension(selectData, data);

                return DataCountVO.builder().save(save).id(id(selectData)).delete(delete).build();
            });
            this.count.set(0);
            current.set(0);
            index++;
            saveAll += dataCountVO.getSave();
            deleteAll += dataCountVO.getDelete();
            id = dataCountVO.getId();
            log.info("迁移数据: " + msg + ", 第{}轮结束, 结束id = {}", index, id);
        }
        log.info("迁移数据:" + msg + "完成, 一共添加{}条记录, 删除重复数据{}条", saveAll, deleteAll);

    }

    private List<R> selectData(AtomicInteger current, Integer size, LocalDateTime start, LocalDateTime end, String id) {
        int i = current.incrementAndGet();
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

        queryWrapper.orderByAsc(idName);
        return getRService().page(new Page<>(i, size, Boolean.FALSE), queryWrapper).getRecords();
    }

    /**
     * 单线程或者多线程执行
     *
     * @return
     * @throws Exception
     */
    private DataCountVO singleOrMultiThread(Supplier<DataCountVO> function) throws Exception {

        //单线程处理
        if (!isThread) {
            // 具体的逻辑处理 抽象出去
            return function.get();
        }

        //多线程处理
        List<Future<DataCountVO>> list = new ArrayList<>();
        for (Integer i = 0; i < threadCount; i++) {
            list.add(pushExecutor.submit(() -> {
                try {
                    // 具体的逻辑处理 抽象出去
                    return function.get();
                } catch (Exception e) {
                    return DataCountVO.builder().throwable(e).build();
                }
            }));
        }

        int save = 0;
        int delete = 0;
        String id = null;
        for (Future<DataCountVO> integerFuture : list) {
            DataCountVO dataCountVO = integerFuture.get();
            throwException(dataCountVO);
            save += dataCountVO.getSave();
            delete += dataCountVO.getDelete();

            if (dataCountVO.getId() != null) {
                if (id == null) {
                    id = dataCountVO.getId();
                } else {
                    id = MaxId(id, dataCountVO.getId());
                }
            }
        }
        return DataCountVO.builder().id(id).save(save).delete(delete).build();
    }

    private String MaxId(String id, String id1) {
        Class<?> type = getIdType();
        if (type.equals(Integer.class)) {
            if (Integer.valueOf(id).compareTo(Integer.valueOf(id1)) > 0) {
                return id;
            } else {
                return id1;
            }
        }
        if (type.equals(Long.class)) {
            if (Long.valueOf(id).compareTo(Long.valueOf(id1)) > 0) {
                return id;
            } else {
                return id1;
            }
        }
        if (type.equals(String.class)) {
            if (id.compareTo(id1) > 0) {
                return id;
            } else {
                return id1;
            }
        }
        throw new RuntimeException("暂不支持该主键id类型！");
    }
}
