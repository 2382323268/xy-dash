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
import lombok.extern.slf4j.Slf4j;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.*;
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
@Slf4j
public class DataPushUtil<T, R> {

    protected static final String PATTERN = "[yyyy-MM-dd HH:mm:ss]" + "[yyyy/M/dd HH:mm:ss]" + "[yyyy/M/dd H:mm:ss]" + "[yyyy/M/dd H:m:ss]" +
            "[yyyy/M/d " +
            "H:mm:ss]" + "[yyyy/M/dd H:m:s]" + "[yyyy/M/d HH:mm:ss]" + "[yyyy-MM-dd]" + "[HH:mm:ss]";

    protected static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);

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

    protected final void throwException(List<Throwable> throwables) {
        throwables.stream().map(Throwable::getMessage).distinct().forEach(e -> {
            log.error("数据迁移异常, 【异常】 = {}", e);
        });
        if (throwables.size() > 0) {
            throwables.get(0).printStackTrace();
            throw new RuntimeException("异常结束, 【异常】 = " + throwables.get(0).getMessage());
        }

    }

    protected static Long IntegerToLong(Integer v) {
        if (v == null) {
            return null;
        }
        return v.longValue();
    }

    protected static Float IntegerToFloat(Integer v) {
        if (v == null) {
            return null;
        }
        return v.floatValue();
    }

    protected static Double IntegerToDouble(Integer v) {
        if (v == null) {
            return null;
        }
        return v.doubleValue();
    }

    protected static Boolean IntegerToBoolean(Integer v) {
        if (v == null) {
            return null;
        }
        return v == 1;
    }

    protected static String IntegerToString(Integer v) {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

    protected static BigDecimal IntegerToBigDecimal(Integer v) {
        if (v == null) {
            return null;
        }
        return BigDecimal.valueOf(v);
    }

    protected static Integer LongToInteger(Long v) {
        if (v == null) {
            return null;
        }
        return v.intValue();
    }

    protected static Float LongToFloat(Long v) {
        if (v == null) {
            return null;
        }
        return v.floatValue();
    }

    protected static Double LongToDouble(Long v) {
        if (v == null) {
            return null;
        }
        return v.doubleValue();
    }


    protected static Boolean LongToBoolean(Long v) {
        if (v == null) {
            return null;
        }
        return v == 1;
    }

    protected static String LongToString(Long v) {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

    protected static Blob LongToBlob(Long v) {
        if (v == null) {
            return null;
        }
        byte[] array = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(v).array();
        try {
            return new SerialBlob(array);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static LocalDate LongToLocalDate(Long v) {
        if (v == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(v), ZoneId.systemDefault()).toLocalDate();
    }

    protected static LocalTime LongToLocalTime(Long v) {
        if (v == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(v), ZoneId.systemDefault()).toLocalTime();
    }

    protected static LocalDateTime LongToLocalDateTime(Long v) {
        if (v == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(v), ZoneId.systemDefault());
    }

    protected static byte[] LongToByte(Long v) {
        if (v == null) {
            return null;
        }
        return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(v).array();
    }

    protected static BigDecimal LongToBigDecimal(Long v) {
        if (v == null) {
            return null;
        }
        return BigDecimal.valueOf(v);
    }

    protected static Integer FloatToInteger(Float v) {
        if (v == null) {
            return null;
        }
        return v.intValue();
    }

    protected static Long FloatToLong(Float v) {
        if (v == null) {
            return null;
        }
        return v.longValue();
    }

    protected static Double FloatToDouble(Float v) {
        if (v == null) {
            return null;
        }
        return v.doubleValue();
    }

    protected static Boolean FloatToBoolean(Float v) {
        if (v == null) {
            return null;
        }
        return v == 1;
    }

    protected static String FloatToString(Float v) {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

    protected static Integer DoubleToInteger(Double v) {
        if (v == null) {
            return null;
        }
        return v.intValue();
    }

    protected static Long DoubleToLong(Double v) {
        if (v == null) {
            return null;
        }
        return v.longValue();
    }

    protected static Float DoubleToFloat(Double v) {
        if (v == null) {
            return null;
        }
        return v.floatValue();
    }

    protected static Boolean DoubleToBoolean(Double v) {
        if (v == null) {
            return null;
        }
        return v == 1;
    }

    protected static Integer BooleanToInteger(Boolean v) {
        if (v == null) {
            return null;
        }
        return v ? 1 : 0;
    }

    protected static Long BooleanToLong(Boolean v) {
        if (v == null) {
            return null;
        }
        return v ? 1L : 0;
    }

    protected static Float BooleanToFloat(Boolean v) {
        if (v == null) {
            return null;
        }
        return v ? 1F : 0;
    }

    protected static Double BooleanToDouble(Boolean v) {
        if (v == null) {
            return null;
        }
        return v ? 1D : 0;
    }

    protected static String BooleanToString(Boolean v) {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

    protected static Integer StringToInteger(String v) {
        if (v == null) {
            return null;
        }
        return Integer.valueOf(v);
    }

    protected static Long StringToLong(String v) {
        if (v == null) {
            return null;
        }
        return Long.valueOf(v);
    }

    protected static Float StringToFloat(String v) {
        if (v == null) {
            return null;
        }
        return Float.valueOf(v);
    }

    protected static Double StringToDouble(String v) {
        if (v == null) {
            return null;
        }
        return Double.valueOf(v);
    }

    protected static Boolean StringToBoolean(String v) {
        if (v == null) {
            return null;
        }
        return Boolean.valueOf(v);
    }

    protected static Blob StringToBlob(String v) {
        if (v == null) {
            return null;
        }
        try {
            return new SerialBlob(v.getBytes(StandardCharsets.UTF_8));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Clob StringToClob(String v) {
        if (v == null) {
            return null;
        }
        try {
            return new SerialClob(v.toCharArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static LocalDate StringToLocalDate(String v) {
        if (v == null) {
            return null;
        }

        return LocalDate.parse(v, dateTimeFormatter);
    }

    protected static LocalTime StringToLocalTime(String v) {
        if (v == null) {
            return null;
        }

        return LocalTime.parse(v, dateTimeFormatter);
    }

    protected static BigDecimal StringToBigDecimal(String v) {
        if (v == null) {
            return null;
        }

        return new BigDecimal(v);
    }


    protected static byte[] StringToByte(String v) {
        if (v == null) {
            return null;
        }

        return v.getBytes(StandardCharsets.UTF_8);
    }


    protected static LocalDateTime StringToLocalDateTime(String v) {
        if (v == null) {
            return null;
        }

        return LocalDateTime.parse(v, dateTimeFormatter);
    }

    protected static byte[] BlobToByte(Blob v) {
        if (v == null) {
            return null;
        }
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(v.getBinaryStream());
            byte[] bytes = new byte[(int) v.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;

            while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }
            return bytes;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                return null;
            }
        }
    }

    protected static String BlobToString(Blob v) {
        if (v == null) {
            return null;
        }
        return new String(BlobToByte(v));
    }

    protected static Long LocalDateToLong(LocalDate v) {
        if (v == null) {
            return null;
        }
        return v.atTime(LocalTime.MIN).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    protected static String LocalDateToString(LocalDate v) {
        if (v == null) {
            return null;
        }
        return v.format(dateTimeFormatter);
    }


    protected static LocalDateTime LocalDateToLocalDateTime(LocalDate v) {
        if (v == null) {
            return null;
        }
        return v.atTime(LocalTime.now());
    }

    protected static String LocalTimeToString(LocalTime v) {
        if (v == null) {
            return null;
        }
        return v.format(dateTimeFormatter);
    }


    protected static LocalDateTime LocalTimeToLocalDateTime(LocalTime v) {
        if (v == null) {
            return null;
        }
        return LocalDateTime.of(LocalDate.now(), v);
    }

    protected static Long LocalDateTimeToLong(LocalDateTime v) {
        if (v == null) {
            return null;
        }
        return v.toInstant(ZoneOffset.UTC).toEpochMilli();
    }


    protected static String LocalDateTimeToString(LocalDateTime v) {
        if (v == null) {
            return null;
        }
        return v.format(df);
    }

    protected static LocalDate LocalDateTimeToLocalDate(LocalDateTime v) {
        if (v == null) {
            return null;
        }
        return v.toLocalDate();
    }

    protected static LocalTime LocalDateTimeToLocalTime(LocalDateTime v) {
        if (v == null) {
            return null;
        }
        return v.toLocalTime();
    }

    protected static Integer BigDecimalToInteger(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.intValue();
    }

    protected static Long BigDecimalToLong(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.longValue();
    }

    protected static Float BigDecimalToFloat(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.floatValue();
    }

    protected static Double BigDecimalToDouble(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.doubleValue();
    }

    protected static Boolean BigDecimalToBoolean(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.compareTo(BigDecimal.valueOf(1)) == 0;
    }

    protected static String BigDecimalToString(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.toString();
    }


    protected static String ByteToString(byte[] v) {
        if (v == null) {
            return null;
        }
        return new String(v);
    }

    protected static Blob ByteToBlob(byte[] v) {
        if (v == null) {
            return null;
        }
        try {
            return new SerialBlob(v);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
