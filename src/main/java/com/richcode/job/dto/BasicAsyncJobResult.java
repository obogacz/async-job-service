package com.richcode.job.dto;

import com.richcode.job.process.AsyncJobResult;
import lombok.Builder;

import java.util.Objects;

@Builder
public record BasicAsyncJobResult(Boolean success, String message) implements AsyncJobResult {

    public static final String DEFAULT_MSG_COMPLETED = "Completed";

    public BasicAsyncJobResult {
        success = Objects.requireNonNullElse(success, true);
        message = Objects.requireNonNullElse(message, DEFAULT_MSG_COMPLETED);
    }

    public static BasicAsyncJobResult completed() {
        return BasicAsyncJobResult.builder()
            .success(true)
            .message(DEFAULT_MSG_COMPLETED)
            .build();
    }

    public static BasicAsyncJobResult failure(String msg) {
        return BasicAsyncJobResult.builder()
            .success(false)
            .message(msg)
            .build();
    }

}
