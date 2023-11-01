package com.richcode.job.process;

import com.richcode.job.dto.AsyncJobConfigurationDto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AsyncJobProcessor {

    private final ThreadPoolTaskExecutor commonTaskExecutor;

    @Autowired
    public AsyncJobProcessor(@Qualifier("commonAsyncJobExecutor") ThreadPoolTaskExecutor commonTaskExecutor) {
        this.commonTaskExecutor = commonTaskExecutor;
    }

    public void submit(@NonNull Runnable job) {
        commonTaskExecutor.execute(job);
    }

    public AsyncJobConfigurationDto getConfiguration() {
        return AsyncJobConfigurationDto.builder()
            .commonExecutorCorePoolSize(commonTaskExecutor.getCorePoolSize())
            .commonExecutorMaxPoolSize(commonTaskExecutor.getMaxPoolSize())
            .build();
    }

    public AsyncJobConfigurationDto updateConfiguration(AsyncJobConfigurationDto config) {
        commonTaskExecutor.setCorePoolSize(config.commonExecutorCorePoolSize());
        commonTaskExecutor.setMaxPoolSize(config.commonExecutorMaxPoolSize());
        return getConfiguration();
    }

}
