package com.richcode.job.dto;

import com.richcode.job.process.AsyncJobParams;
import com.richcode.job.process.AsyncJobResult;
import lombok.Builder;

import java.time.Duration;
import java.time.OffsetDateTime;

@Builder
public record AsyncJobSummaryDto<PARAM extends AsyncJobParams, RES extends AsyncJobResult>(
    Long id,
    AsyncJobType type,
    AsyncJobStatus status,
    String contextId,
    String username,
    PARAM parameters,
    RES result,
    OffsetDateTime created,
    OffsetDateTime updated,
    Duration duration
) {
}
