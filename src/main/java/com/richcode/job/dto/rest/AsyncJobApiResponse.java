package com.richcode.job.dto.rest;

import lombok.Builder;

import java.util.Objects;

@Builder
public record AsyncJobApiResponse(Boolean success, String message) {

    public AsyncJobApiResponse {
        success = Objects.requireNonNullElse(success, true);
    }

    public static AsyncJobApiResponse ok() {
        return AsyncJobApiResponse.builder()
            .success(true)
            .build();
    }

}
