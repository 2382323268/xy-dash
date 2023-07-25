package com.xy.dash.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolExecutorConfig {

    @Value("${task.core-pool-size}")
    private Integer corePoolSize;

    @Value("${task.max-pool-size}")
    private Integer maxPoolSize;

    @Value("${task.queue-capacity}")
    private Integer queueCapacity;

    @Value("${task.keep-alive-seconds}")
    private Integer keepAliveSeconds;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new com.xy.data.config.thread.VisiableThreadPoolTaskExecutor();
        //核心池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //队列程度
        executor.setQueueCapacity(queueCapacity);
        //线程空闲时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //线程前缀名称
        executor.setThreadNamePrefix("xy-dash");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}

