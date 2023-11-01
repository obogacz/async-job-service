package com.richcode.job.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum AsyncJobStatus {

    PENDING(0, false),
    QUEUED(1, false),
    FAILURE(2, true),
    SUCCESS(3, true);

    private final int priority;
    private final boolean isFinal;

    public static final Comparator<AsyncJobStatus> COMMON_PRIORITY_COMPARATOR = Comparator
            .comparingInt(AsyncJobStatus::getPriority)
            .reversed();

    public static final Set<AsyncJobStatus> IN_PROGRESS_STATUSES = EnumSet.of(QUEUED, PENDING);

    boolean isInProgress() {
        return this == QUEUED || this == PENDING;
    }

    boolean isSuccess() {
        return this == SUCCESS;
    }

    boolean isFailure() {
        return this == FAILURE;
    }
    
}
