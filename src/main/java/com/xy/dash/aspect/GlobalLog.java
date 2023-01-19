package com.xy.dash.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: xiangwei
 * @Date: 2023/1/3 22:08
 * @Description
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface GlobalLog {
    String value() default "GlobalLog";
}
