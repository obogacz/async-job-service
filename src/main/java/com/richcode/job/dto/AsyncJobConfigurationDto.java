package com.richcode.job.dto;

import lombok.Builder;

@Builder
public record AsyncJobConfigurationDto(Integer commonExecutorCorePoolSize,
                                       Integer commonExecutorMaxPoolSize,
                                       Integer fooExecutorCorePoolSize,
                                       Integer fooExecutorMaxPoolSize) {
}
