//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.dash.utli;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.util.*;

public class BeanUtil extends BeanUtils {
    public BeanUtil() {
    }

    public static <T> T newInstance(Class<T> clazz) {
        return instantiateClass(clazz);
    }

    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = ClassUtils.forName(clazzStr, (ClassLoader)null);
            return (T) newInstance(clazz);
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException(var2);
        }
    }




    @Nullable
    public static <T> T copyProperties(@Nullable Object source, Class<T> targetClazz) throws BeansException {
        if (source == null) {
            return null;
        } else {
            T to = newInstance(targetClazz);
            copyProperties((Object)source, (Object)to);
            return to;
        }
    }

    public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList, Class<T> targetClazz) throws BeansException {
        if (sourceList != null && !sourceList.isEmpty()) {
            List<T> outList = new ArrayList(sourceList.size());
            Iterator var3 = sourceList.iterator();

            while(var3.hasNext()) {
                Object source = var3.next();
                if (source != null) {
                    T bean = copyProperties(source, targetClazz);
                    outList.add(bean);
                }
            }

            return outList;
        } else {
            return Collections.emptyList();
        }
    }


}
