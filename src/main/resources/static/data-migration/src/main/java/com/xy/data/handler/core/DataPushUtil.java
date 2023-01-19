package com.xy.data.handler.core;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.data.util.DataPushConstant;
import com.xy.data.util.SpringUtil;
import com.xy.data.vo.DataCountVO;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2023/1/6 14:33
 * @Description
 **/
public class DataPushUtil<T, R> {

    protected final String PATTERN = "[yyyy-MM-dd HH:mm:ss]" + "[yyyy/M/dd HH:mm:ss]" + "[yyyy/M/dd H:mm:ss]" + "[yyyy/M/dd H:m:ss]" + "[yyyy/M/d " +
            "H:mm:ss]" + "[yyyy/M/dd H:m:s]" + "[yyyy/M/d HH:mm:ss]";

    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
    protected DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    protected BaseMapper<T> baseMapper = null;

    protected IService<T> tiService = null;

    protected IService<R> riService = null;

    private String id = null;

    private Class<?> idType = null;

    private String createdTime = null;

    private String dsName = null;

    private Field fid = null;

    private Field tid = null;

    private Field fcreatedTime = null;

    protected Class<T> tClass;

    protected Class<R> rClass;

    protected AtomicInteger count = new AtomicInteger(0);

    protected CyclicBarrier cyclicBarrier = null;

    private Class<?> mapper;


    public DataPushUtil(Class<T> tClass, Class<R> rClass, Class<?> mapper) {
        this.tClass = tClass;
        this.rClass = rClass;
        this.mapper = mapper;
    }

    protected QueryWrapper<R> queryWrapper() {
        return new QueryWrapper<>();
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


    private Field getFieldR(String name) {
        Field field = null;
        try {
            field = rClass.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
        field.setAccessible(true);
        return field;
    }


    protected final String getDsName() {
        if (dsName == null) {
            if (tClass.isAnnotationPresent(DS.class)) {
                dsName = tClass.getAnnotation(DS.class).value();
            } else {
                dsName = "master";
            }
        }

        return dsName;
    }

    protected final BaseMapper<T> getMapper() {
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

    protected final IService<T> getTService() {
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

    protected final Field getTId() {
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

    protected final void throwException(DataCountVO dataCountVO){
        if (dataCountVO.getThrowable() != null) {
            dataCountVO.getThrowable().printStackTrace();
            throw new RuntimeException("数据迁移失败！");
        }
    }
}
