package com.richcode.job.dto;

import com.richcode.job.process.AsyncJobParams;
import lombok.Builder;

@Builder
public record BasicAsyncJobParams(String contextId, String username) implements AsyncJobParams {
}
