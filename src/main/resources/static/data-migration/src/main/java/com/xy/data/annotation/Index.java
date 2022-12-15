package com.xy.data.annotation;

import java.lang.annotation.*;

/**
 * @Author: xiangwei
 * @Date: 2022/11/8 18:34
 * @Description
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Index {
}
