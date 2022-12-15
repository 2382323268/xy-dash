//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.data.util;

import com.baomidou.mybatisplus.extension.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class
SpringUtil implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(SpringUtil.class);
    private static ApplicationContext context;

    public SpringUtil() {
    }

    public static <T> List<T> getBeanList(Class<T> clazz) {
        List<T> beanList = new ArrayList<>();
        String[] beanNamesForType = context.getBeanNamesForType(clazz);
        if (beanNamesForType == null) {
            return null;
        }
        for (String beanName : beanNamesForType) {
            T bean = (T) getBean(beanName);

            log.info("处理数据的Handler, beanName = {}", beanName);
            beanList.add(bean);
        }
        return beanList;
    }

    public static <T, R> IService getServiceBean(Class<R> type) {
        return (IService) getBean(IService.class, type, 1);
    }


    public static <T, R> T getBean(Class<T> clazz, Class<R> type, int index) {
        String[] beanNamesForType = context.getBeanNamesForType(clazz);
        if (beanNamesForType == null) {
            return null;
        }
        for (String beanName : beanNamesForType) {
            T bean = (T) getBean(beanName);
            Type genericSuperclass = bean.getClass().getSuperclass().getGenericSuperclass();
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments[index].equals(type)) {
                return bean;
            }
        }
        return null;
    }

    public static <T> T getBean(Class<T> clazz) {
        return clazz == null ? null : context.getBean(clazz);
    }

    public static <T> T getBean(String beanId) {
        return beanId == null ? null : (T) context.getBean(beanId);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (null != beanName && !"".equals(beanName.trim())) {
            return clazz == null ? null : context.getBean(beanName, clazz);
        } else {
            return null;
        }
    }

    public static ApplicationContext getContext() {
        return context == null ? null : context;
    }

    public static void publishEvent(ApplicationEvent event) {
        if (context != null) {
            try {
                context.publishEvent(event);
            } catch (Exception var2) {
                log.error(var2.getMessage());
            }

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.context = applicationContext;
    }
}
