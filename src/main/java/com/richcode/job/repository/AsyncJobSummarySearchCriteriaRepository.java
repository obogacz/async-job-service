package com.richcode.job.repository;

import com.richcode.job.domain.AsyncJobSummary;
import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.support.SearchRange;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collection;

public interface AsyncJobSummarySearchCriteriaRepository {

    Page<AsyncJobSummary> findPage(AsyncJobSummarySearchCriteria criteria);

    @Builder(toBuilder = true)
    record AsyncJobSummarySearchCriteria(
        Collection<Long> idIn,
        Collection<String> contextIdIn,
        Collection<AsyncJobType> typeIn,
        Collection<AsyncJobStatus> statusIn,
        Collection<String> usernameIn,
        SearchRange<OffsetDateTime> createdBetween,
        SearchRange<OffsetDateTime> updatedBetween,
        Pageable page
    ) {
    }

}
