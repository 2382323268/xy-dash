package com.xy.dash.utli.api;

import com.xy.dash.utli.exception.BladeRestExceptionTranslator;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice(basePackages = {"com.xy.dash"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResultAdvice implements ResponseBodyAdvice<Object> {

    private final List<String> classWhiteList;

    RestResultAdvice() {
        classWhiteList = Arrays.asList("SwaggerConfiguration");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> returnClass = returnType.getDeclaringClass();
        if (returnClass.isAssignableFrom(BladeRestExceptionTranslator.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!MediaType.APPLICATION_JSON.isCompatibleWith(selectedContentType)) {
            return body;
        }

        if (body instanceof R) {
            return body;
        }
        for (String s : classWhiteList) {
            if (returnType.getDeclaringClass().getName().contains(s)) {
                return body;
            }
        }

        return R.data(body);
    }
}
