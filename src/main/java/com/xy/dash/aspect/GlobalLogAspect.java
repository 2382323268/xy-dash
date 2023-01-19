package com.xy.dash.aspect;

import com.alibaba.fastjson.JSON;
import com.xy.dash.aspect.GlobalLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author: xiangwei
 * @Date: 2023/1/3 21:48
 * @Description
 **/
@Component
@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue ="dev")
@Aspect
@Slf4j
public class GlobalLogAspect {

    @Around("@annotation(com.xy.dash.aspect.GlobalLog)")
    public Object recordAccess(ProceedingJoinPoint pjp) throws Throwable {
        return Handler(pjp);
    }


    @Around("@within(com.xy.dash.aspect.GlobalLog)")
    public Object recordAccess1(ProceedingJoinPoint pjp) throws Throwable {
        return Handler(pjp);
    }
    private Object Handler(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        GlobalLog annotation = methodSignature.getMethod().getAnnotation(GlobalLog.class);
        try {
            log.info("{} [{}.{}], 入参: [{}]", annotation.value(), signature.getDeclaringTypeName(), signature.getName(), flatArgs(pjp));
            long startMs = System.currentTimeMillis();

            Object result = pjp.proceed();
            log.info("{} 执行耗时 [{}], 出参 [{}]", annotation.value(), System.currentTimeMillis() - startMs, JSON.toJSONString(result));
            return result;
        } catch (Throwable e) {
            exception(e, annotation.value());
            throw e;
        }
    }

    private void exception(Throwable e, String value) {
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            String className = stackTraceElement.getClassName();
            if (className.contains("com.yuexiuproperty") && !className.contains("GlobalLogAspect")) {
                log.error("{} 异常: 类名 [{}], 方法名 [{}], 方法错误行数 ({}:{})", value, className, stackTraceElement.getMethodName(),
                        stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
            }
        }

    }

    private String flatArgs(ProceedingJoinPoint pjp) {
        String[] parameterNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
        if (CollectionUtils.isEmpty(Arrays.asList(parameterNames))) {
            return "";
        }
        StringBuilder srt = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            Object[] args = pjp.getArgs();
            srt.append(parameterNames[i] + " :");
            srt.append(" " + args[i] + ", ");
        }
        srt.replace(srt.length() - 2, srt.length() - 1, "");
        return srt.toString();
    }

}
