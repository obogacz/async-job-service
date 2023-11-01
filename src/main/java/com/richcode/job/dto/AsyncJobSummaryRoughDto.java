package com.richcode.job.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.OffsetDateTime;

@Builder
public record AsyncJobSummaryRoughDto(
    Long id,
    AsyncJobType type,
    AsyncJobStatus status,
    String contextId,
    String username,
    String parameters,
    String result,
    OffsetDateTime created,
    OffsetDateTime updated,
    Duration duration
) {
}
