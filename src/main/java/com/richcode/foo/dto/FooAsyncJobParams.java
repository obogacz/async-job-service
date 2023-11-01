package com.richcode.foo.dto;

import com.richcode.job.process.AsyncJobParams;
import lombok.Builder;

@Builder
public record FooAsyncJobParams(
    String contextId,
    String username,
    String fooParam
) implements AsyncJobParams {
}
