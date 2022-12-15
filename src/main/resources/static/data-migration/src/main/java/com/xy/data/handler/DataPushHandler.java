package com.xy.data.handler;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.data.util.DataPushConstant;
import com.xy.data.util.DistributeTransactionService;
import com.xy.data.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/7/16 19:02
 * @Description
 **/
@Slf4j
public abstract class DataPushHandler<T, R> {

    protected final String PATTERN = "[yyyy-MM-dd HH:mm:ss]" + "[yyyy/M/dd HH:mm:ss]" + "[yyyy/M/dd H:mm:ss]" + "[yyyy/M/dd H:m:ss]" + "[yyyy/M/d " +
            "H:mm:ss]" + "[yyyy/M/dd H:m:s]" + "[yyyy/M/d HH:mm:ss]";

    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
    protected DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    protected Class<T> tClass;

    protected Class<R> rClass;


    protected BaseMapper<T> baseMapper = null;

    protected IService<T> tiService = null;

    protected IService<R> riService = null;

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

    private Class<?> mapper;

    private String id = null;

    private Class<?> idType = null;

    private String start = null;

    private String end = null;

    private String createdTime = null;

    private String dsName = null;

    private Field fid = null;

    private Field tid = null;

    private Field fcreatedTime = null;

    private Long sleep = 0L;

    protected AtomicInteger count = new AtomicInteger(0);
    //  private volatile int count = 0;

    @Autowired
    protected ThreadPoolTaskExecutor pushExecutor;

    public DataPushHandler(Class<T> tClass, Class<R> rClass, Class<?> mapper, String start, String end) {
        this.tClass = tClass;
        this.rClass = rClass;
        this.mapper = mapper;
        this.start = start;
        this.end = end;
    }

    protected abstract List<T> getData(List<R> data);

    protected abstract void deleteBeforeExtension(List<R> r, List<T> t);

    protected abstract void saveBeforeExtension(List<R> r, List<T> t);

    protected abstract void saveAfterExtension(List<R> r, List<T> t);

    protected abstract void pushData(String msg, LocalDateTime start, LocalDateTime end) throws Exception;

    protected QueryWrapper<R> queryWrapper() {
        return new QueryWrapper<>();
    }

    protected final void push() {
        push(start == null ? null : LocalDateTime.parse(start, df), end == null ? null : LocalDateTime.parse(end, df));
    }

    protected final void push(LocalDateTime start, LocalDateTime end) {
        String msg = tClass.getDeclaredAnnotation(TableName.class).value();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            pushData(msg, start, end);
            log.info("迁移{}表耗时: {}", msg, (System.currentTimeMillis() - currentTimeMillis) / 1000 + "s");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("迁移数据" + msg + "异常, e = {}", e.getMessage());
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

    protected final int total(LocalDateTime start, LocalDateTime end) {
        String createdTime = getCreatedTime();
        if (createdTime == null) {
            return getRService().count(queryWrapper());
        }
        boolean timestamp = !getFCreatedTime().getType().equals(LocalDateTime.class);
        QueryWrapper<R> le = queryWrapper().ge(start != null, createdTime, timestamp ? asLong(start) : start)
                .le(end != null, createdTime, timestamp ? asLong(end) : end);
        int total = getRService().count(le);
        return total;
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
    protected final void block() {
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

    protected final void block(Integer size, Integer number) {
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

    private Field getFieldR(String name) {
        Field field = null;
        try {
            field = rClass.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        field.setAccessible(true);
        return field;
    }


    private String getDsName() {
        if (dsName == null) {
            if (tClass.isAnnotationPresent(DS.class)) {
                dsName = tClass.getAnnotation(DS.class).value();
            } else {
                dsName = "master";
            }
        }

        return dsName;
    }

    private BaseMapper<T> getMapper() {
        if (this.baseMapper == null) {
            this.baseMapper = (BaseMapper<T>) SpringUtil.getBean(mapper);
        }
        return this.baseMapper;
    }

    protected final IService<R> getRService() {
        if (this.riService == null) {
            this.riService = SpringUtil.getServiceBean(rClass);
        }
        return this.riService;
    }

    private final IService<T> getTService() {
        if (this.tiService == null) {
            this.tiService = SpringUtil.getServiceBean(tClass);
        }
        return this.tiService;
    }

    protected final String getId() {
        if (this.id == null) {
            this.id = getFieldR(DataPushConstant.ID).getAnnotation(TableId.class).value();
        }
        return this.id;
    }

    private final Field getTId() {
        if (this.tid == null) {
            Field field = null;
            try {
                field = tClass.getDeclaredField(DataPushConstant.ID);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            this.tid = field;
        }
        return this.tid;
    }

    protected final String getCreatedTime() {
        if (this.createdTime == null) {
            Field fieldR = getFieldR(DataPushConstant.TIME);
            if (fieldR == null) {
                return null;
            }
            TableField annotation = fieldR.getAnnotation(TableField.class);
            if (annotation == null) {
                return null;
            }
            this.createdTime = annotation.value();
        }
        return this.createdTime;
    }

    protected final Field getFId() {
        if (this.fid == null) {
            this.fid = getFieldR(DataPushConstant.ID);
        }
        return this.fid;
    }

    protected final Field getFCreatedTime() {
        if (this.fcreatedTime == null) {
            this.fcreatedTime = getFieldR(DataPushConstant.TIME);
        }
        return this.fcreatedTime;
    }

    protected final LocalDateTime asLocalDateTime(Long time) {
        if (time == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    protected final Long asLong(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    protected final List<String> neIds(LocalDateTime startDateTime, List<R> data) {
        Long startTime = asLong(startDateTime);
        Field id = getFId();
        Field createdTime = getFCreatedTime();
        boolean timestamp = !createdTime.getType().equals(LocalDateTime.class);
        if (timestamp) {
            data = data.stream().filter(e -> {
                try {
                    Long aLong = (Long) createdTime.get(e);
                    return aLong.compareTo(startTime) == 0;
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException("反射获取createdTime报错");
            }).collect(Collectors.toList());
        } else {
            data = data.stream().filter(e -> {
                try {
                    LocalDateTime aLong = (LocalDateTime) createdTime.get(e);
                    return aLong.compareTo(startDateTime) == 0;
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException("反射获取createdTime报错");
            }).collect(Collectors.toList());
        }

        return data.stream().map(e -> {
            try {
                return id.get(e).toString();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("反射获取id报错");
        }).collect(Collectors.toList());
    }

    protected final LocalDateTime startDateTime(List<R> data) {
        try {
            Field createdTime = getFCreatedTime();
            boolean timestamp = !createdTime.getType().equals(LocalDateTime.class);
            R last = data.get(data.size() - 1);
            if (timestamp) {

                return asLocalDateTime((Long) createdTime.get(last));

            }
            return (LocalDateTime) createdTime.get(last);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("反射获取createdTime失败");
    }

    protected final String id(List<R> data) {
        try {
            Field id = getFId();
            R last = data.get(data.size() - 1);

            return id.get(last).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("反射获取id失败");
    }

    protected final Class<?> getIdType() {
        if (idType == null) {
            idType = getFId().getType();
        }
        return idType;
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

    public static void run() {
        Long start = System.currentTimeMillis();
        List<DataPushHandler> beanList = SpringUtil.getBeanList(DataPushHandler.class);
        beanList.forEach(DataPushHandler::push);
        log.info("一共耗时: {}", (System.currentTimeMillis() - start) / 1000 + "s");
        log.info("正在停止服务器！");
    }

}
