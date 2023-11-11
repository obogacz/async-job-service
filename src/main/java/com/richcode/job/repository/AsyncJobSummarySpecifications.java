package com.richcode.job.repository;

import com.richcode.job.domain.AsyncJobSummary;
import com.richcode.job.domain.AsyncJobSummary_;
import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.support.SearchRange;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AsyncJobSummarySpecifications {

    public static Specification<AsyncJobSummary> hasIdIn(Collection<Long> ids) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(ids)) {
                return missingCriteria();
            }
            return root
                .get(AsyncJobSummary_.id)
                .in(ids);
        };
    }

    public static Specification<AsyncJobSummary> hasContextIdIn(Collection<String> contextIds) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(contextIds)) {
                return missingCriteria();
            }
            return root
                .get(AsyncJobSummary_.contextId)
                .in(contextIds);
        };
    }

    public static Specification<AsyncJobSummary> hasTypeIn(Collection<AsyncJobType> types) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(types)) {
                return missingCriteria();
            }
            return root
                .get(AsyncJobSummary_.type)
                .in(types);
        };
    }

    public static Specification<AsyncJobSummary> hasUsernameIn(Collection<String> usernames) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(usernames)) {
                return missingCriteria();
            }
            return root
                .get(AsyncJobSummary_.username)
                .in(usernames);
        };
    }

    public static Specification<AsyncJobSummary> hasStatusIn(Collection<AsyncJobStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(statuses)) {
                return missingCriteria();
            }
            return root
                .get(AsyncJobSummary_.status)
                .in(statuses);
        };
    }

    public static Specification<AsyncJobSummary> hasCreatedBetween(SearchRange<OffsetDateTime> range) {
        return (root, query, criteriaBuilder) -> {
            if (SearchRange.isEmpty(range)) {
                return missingCriteria();
            }
            return criteriaBuilder.between(root.get(AsyncJobSummary_.created), range.from(), range.to());
        };
    }

    public static Specification<AsyncJobSummary> hasUpdatedBetween(SearchRange<OffsetDateTime> range) {
        return (root, query, criteriaBuilder) -> {
            if (SearchRange.isEmpty(range)) {
                return missingCriteria();
            }
            return criteriaBuilder.between(root.get(AsyncJobSummary_.updated), range.from(), range.to());
        };
    }

    private static Predicate missingCriteria() {
        return null;
    }
}
