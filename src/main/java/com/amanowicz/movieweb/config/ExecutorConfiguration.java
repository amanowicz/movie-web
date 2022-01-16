package com.amanowicz.movieweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;

@Configuration
public class ExecutorConfiguration {

    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 10;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ThreadFactory threadFactory = new CustomizableThreadFactory();
        executor.setThreadFactory(threadFactory);
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        TaskExecutor taskExecutor = new ConcurrentTaskExecutor(executor);
        executor.initialize();
        return taskExecutor;
    }
}
