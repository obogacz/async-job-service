package com.richcode.job.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
class AsyncJobConfiguration {

    @Value("${job.executor.common.corePoolSize:3}")
    private int commonAsyncJobExecutorCorePoolSize;
    @Value("${job.executor.common.maxPoolSize:6}")
    private int commonAsyncJobExecutorMaxPoolSize;

    @Bean
    public ThreadPoolTaskExecutor commonAsyncJobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadGroupName("common-async-job-executor");
        executor.setThreadNamePrefix("common-thread-");
        executor.setCorePoolSize(commonAsyncJobExecutorCorePoolSize);
        executor.setMaxPoolSize(commonAsyncJobExecutorMaxPoolSize);
        return executor;
    }

}
