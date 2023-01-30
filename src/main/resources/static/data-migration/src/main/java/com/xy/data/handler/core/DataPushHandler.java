package com.xy.data.handler.core;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xy.data.util.DistributeTransactionService;
import com.xy.data.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/7/16 19:02
 * @Description
 **/
@Slf4j
public abstract class DataPushHandler<T, R> extends DataPushUtil<T, R> {
    @Value("${data.config.threadCount}")
    protected Integer threadCount;

    @Value("${data.config.sqlSpliec}")
    protected boolean sqlSpliec;

    @Value("${data.config.isThread}")
    protected boolean isThread;

    @Value("${data.config.isDelete}")
    protected boolean isDelete;

    @Value("${data.config.limit}")
    protected Integer limit;

    private Long sleep = 0L;

    private String start = null;

    private String end = null;

    @Autowired
    protected ThreadPoolTaskExecutor pushExecutor;

    public DataPushHandler(Class<T> tClass, Class<R> rClass, Class<?> mapper, String start, String end) {
        super(tClass, rClass, mapper);

        this.end = end;
        this.start = start;
    }

    protected abstract List<T> getData(List<R> data);

    protected abstract void deleteBeforeExtension(List<R> r, List<T> t);

    protected abstract void saveBeforeExtension(List<R> r, List<T> t);

    protected abstract void saveAfterExtension(List<R> r, List<T> t);

    protected abstract void pushData(String msg, LocalDateTime start, LocalDateTime end) throws Exception;

    private void push() {
        init();
        push(start == null ? null : LocalDateTime.parse(start, df), end == null ? null : LocalDateTime.parse(end, df));
    }

    private void push(LocalDateTime start, LocalDateTime end) {
        String tName = tClass.getDeclaredAnnotation(TableName.class).value();
        String tDs = tClass.getDeclaredAnnotation(DS.class).value();
        String rDs = rClass.getDeclaredAnnotation(DS.class).value();
        String rName = rClass.getDeclaredAnnotation(TableName.class).value();
        StringBuilder msg = new StringBuilder();
        msg.append("数据源:").append(tDs).append(" 表名:").append(tName).append("迁移到---").append("数据源:").append(rDs).append(" 表名:").append(rName);
        try {
            long currentTimeMillis = System.currentTimeMillis();
            pushData(msg.toString(), start, end);
            log.info(msg + "耗时: {}", (System.currentTimeMillis() - currentTimeMillis) / 1000 + "s");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(msg + "数据异常, e = {}", e.getMessage());
        }
    }

    protected final int saveAll(List<T> data) {
        if (sqlSpliec) {
            String dsName = getDsName();
            DistributeTransactionService.batchSave(data, dsName);
        } else {
            if (!getTService().saveBatch(data)) {
                throw new RuntimeException("保存数据失败！");
            }
        }
        return data.size();
    }

    protected final int deleteByIds(List<T> data) {
        if (!isDelete) {
            return 0;
        }
        Field id = getTId();

        List<String> ids = data.stream().map(e -> {
            try {
                Object o = id.get(e);
                if (o == null) {
                    return null;
                }
                return o.toString();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("反射获取id报错");
        }).collect(Collectors.toList());

        return getMapper().deleteBatchIds(ids);
    }

    /**
     * 1.多线程需要等所有线程删除完再走下一步
     * 2.删除+索引失效 会表锁
     * 3.导致事务死锁
     */
    @Deprecated
    protected void block() {
        if (isThread && isDelete) {

            this.count.incrementAndGet();
            while (count.get() < threadCount) {
                try {
                    // while（true） 循环cdp占用率太高 volatile也不能保证变量刷回主内存
                    Thread.sleep(sleep());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Deprecated
    protected void block(Integer size, Integer number) {
        if (isThread && isDelete) {
            int i = size / number;
            this.count.incrementAndGet();
            while (count.get() < i) {
                try {
                    // while（true） 循环cdp占用率太高 volatile也不能保证变量刷回主内存
                    Thread.sleep(sleep());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Deprecated
    protected void blockOperation() {
        this.count.incrementAndGet();
    }

    protected final void await(Integer size, Integer number) {
        if (isThread && isDelete) {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    protected final void await() {
        if (isThread && isDelete) {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void cyclicBarrier() {
        if (isThread && isDelete) {
            if (cyclicBarrier == null) {
                cyclicBarrier = new CyclicBarrier(threadCount);
            }
        }
    }

    private long sleep() {
        if (sleep == null) {
            if (threadCount > 200) {
                sleep = 200L;
            }
            if (threadCount < 50) {
                sleep = 50L;
            }
            if (200 < threadCount && threadCount > 50) {
                sleep = Long.valueOf(threadCount);
            }
        }
        return sleep;
    }

    private void init() {
        cyclicBarrier();
        getFCreatedTime();
        getFId();
        getCreatedTime();
        getTId();
        getId();
        getTService();
        getRService();
        getMapper();
        getDsName();
    }

    public static void run() {
        Long start = System.currentTimeMillis();
        List<DataPushHandler> beanList = SpringUtil.getBeanList(DataPushHandler.class);
        beanList.forEach(DataPushHandler::push);
        log.info("一共耗时: {}", (System.currentTimeMillis() - start) / 1000 + "s");
        log.info("正在停止服务器！");
    }

}
