package com.xy.dash.utli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/4/14 11:56
 * @Description
 **/
@Slf4j
public class RedisUtils {
    private static RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_LOCK = "Redis_Lock_YueXiu:";

    static {
        redisTemplate = SpringUtil.getBean("redisTemplate");
    }

    public static void addZSet(String key, String val, long source, long timeOut, TimeUnit unit) {

        redisTemplate.opsForZSet().add(key, val, source);
        if (Objects.nonNull(unit)) {
            RedisUtils.getRedisTemplate().expire(key, timeOut, TimeUnit.DAYS);
        }
    }

    public static Long zSetCount(String key, long windowStartMs, long currentMs) {
        return redisTemplate.opsForZSet().count(key, windowStartMs, currentMs);
    }

    public static void addSetElement(String key, Object val) {
        redisTemplate.opsForSet().add(key, val);
    }

    public static void removeSetElement(String key, Object val) {
        redisTemplate.opsForSet().remove(key, val);
    }

    public static boolean setElementIsMember(String key, String val) {
        Boolean member = redisTemplate.opsForSet().isMember(key, val);
        if (Objects.isNull(member)) {
            return false;
        }
        return member;
    }


    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static void set(String key, Object value, long expirationTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expirationTime, timeUnit);
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static <T> T get(String key, long expirationTime, TimeUnit timeUnit, Supplier<T> supplier) {
        Object o = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(o)) {
            o = supplier.get();
            if (o == null) {
                return null;
            }
            set(key, o, expirationTime, timeUnit);
        }
        return (T) o;
    }

    public static <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static <T> void setList(String key, List<T> value, long expirationTime, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPushAll(key, (Collection) value);
        redisTemplate.expire(key, expirationTime, timeUnit);
    }

    public static <T> List<T> getList(String key, long expirationTime, TimeUnit timeUnit, Integer skip, Integer limit,
                                      Supplier<List<T>> supplier) {
        List<T> o = null;
        boolean b = skip == null || limit == null;
        if (b) {
            o = (List<T>) redisTemplate.opsForList().range(key, 0, -1);
        } else {

            o = (List<T>) redisTemplate.opsForList().range(key, skip, skip + limit - 1);
        }

        if (ObjectUtils.isEmpty(o)) {
            o = supplier.get();
            if (o == null) {
                return o;
            }
            setList(key, o, expirationTime, timeUnit);
            if (!b) {
                o = o.stream().skip(skip).limit(limit).collect(Collectors.toList());
            }
        }
        return o;
    }

    public static <T> List<T> getList(String key, long expirationTime, TimeUnit timeUnit, Supplier<List<T>> supplier) {
        return getList(key, expirationTime, timeUnit, null, null, supplier);
    }

    public static Long getListSize(String key) {
        return redisTemplate.opsForList().size(key) == null ? 0 : redisTemplate.opsForList().size(key);
    }

    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public static Boolean lock(String suffixKey, long expirationTime, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK.concat(suffixKey), value, expirationTime, TimeUnit.MILLISECONDS);
    }

    public static Boolean lock(String suffixKey, long expirationTime) {
        return lock(suffixKey, expirationTime, 0);
    }

    public static void lock(String suffixKey, long expirationTime, FunctionalInterface normal) {
        lock(suffixKey, expirationTime, normal, null);
    }

    public static void lock(String suffixKey, long expirationTime, FunctionalInterface normal, FunctionalInterface special) {
        String lock = null;
        try {
            lock = reqLock(suffixKey, expirationTime);
            if (StringUtils.isNotEmpty(lock)) {
                normal.run();
            } else {
                if (special != null) {
                    special.run();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            releaseReqLock(suffixKey, lock);
        }
    }

    public static Boolean releaseLock(String suffixKey) {
        return redisTemplate.delete(REDIS_LOCK.concat(suffixKey));
    }

    public static String reqLock(String suffixKey, long expirationTime) {
        String requestId = UUID.randomUUID().toString();
        if (lock(suffixKey, expirationTime, requestId)) {
            return requestId;
        }
        return null;
    }

    public static void releaseReqLock(String suffixKey, String requestId) {
        if (StringUtils.isBlank(requestId)) {
            return;
        }
        String key = REDIS_LOCK.concat(suffixKey);
        Object value = get(key);
        if (Objects.nonNull(value) && value.equals(requestId)) {
            redisTemplate.delete(key);
        }
    }

}
