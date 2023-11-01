package com.richcode.job.repository;

import com.richcode.job.domain.AsyncJobSummary;
import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.richcode.job.repository.AsyncJobSummarySpecifications.*;
import static java.util.Objects.nonNull;

public interface AsyncJobSummaryRepository extends
    JpaRepository<AsyncJobSummary, Long>,
    JpaSpecificationExecutor<AsyncJobSummary>,
    AsyncJobSummarySearchCriteriaRepository {

    Pageable DEFAULT_PAGE = PageRequest.ofSize(Integer.MAX_VALUE);

    default Page<AsyncJobSummary> findPage(AsyncJobSummarySearchCriteria criteria) {
        Pageable page = nonNull(criteria.page()) ? criteria.page() : DEFAULT_PAGE;
        Specification<AsyncJobSummary> spec = Specification
            .where(hasIdIn(criteria.idIn()))
            .and(hasContextIdIn(criteria.contextIdIn()))
            .and(hasTypeIn(criteria.typeIn()))
            .and(hasStatusIn(criteria.statusIn()))
            .and(hasUsernameIn(criteria.usernameIn()))
            .and(hasCreatedBetween(criteria.createdBetween()))
            .and(hasUpdatedBetween(criteria.updatedBetween()));
        return findAll(spec, page);
    }

    Optional<AsyncJobSummary> findByIdAndType(Long id, AsyncJobType type);
    List<AsyncJobSummary> findByContextId(String contextId);
    List<AsyncJobSummary> findByContextIdAndTypeIn(String contextId, Set<AsyncJobType> types);
    List<AsyncJobSummary> findByContextIdAndTypeAndStatusIn(String contextId, AsyncJobType type, Set<AsyncJobStatus> statuses);
    Optional<AsyncJobSummary> findFirstByContextIdAndTypeOrderByCreatedDesc(String contextId, AsyncJobType type);
    Optional<AsyncJobSummary> findFirstByContextIdAndTypeAndStatusOrderByCreatedDesc(String contextId, AsyncJobType type, AsyncJobStatus status);
    List<AsyncJobSummary> findByStatusIn(Set<AsyncJobStatus> statuses);

}
