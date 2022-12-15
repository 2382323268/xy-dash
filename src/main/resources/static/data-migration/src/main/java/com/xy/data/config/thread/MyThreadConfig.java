package com.xy.data.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class MyThreadConfig {

    @Value("${data.config.threadCount}")
    private Integer threadCount;

    @Bean
    public ThreadPoolTaskExecutor pushExecutor() {
        ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        //核心池大小
        executor.setCorePoolSize(threadCount);
        //最大线程数
        executor.setMaxPoolSize(threadCount * 2);
        //队列程度
        executor.setQueueCapacity(10000);
        //线程空闲时间
        executor.setKeepAliveSeconds(10);
        //线程前缀名称
        executor.setThreadNamePrefix("yuexiuhui-data-push");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}

