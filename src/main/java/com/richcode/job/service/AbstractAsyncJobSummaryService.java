package com.richcode.job.service;

import com.richcode.job.converter.AsyncJobSummaryConverter;
import com.richcode.job.converter.AsyncJobSummaryJsonMapper;
import com.richcode.job.domain.AsyncJobSummary;
import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobSummaryDto;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.job.process.AsyncJobParams;
import com.richcode.job.process.AsyncJobResult;
import com.richcode.job.repository.AsyncJobSummaryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public abstract class AbstractAsyncJobSummaryService<PARAM extends AsyncJobParams, RES extends AsyncJobResult> {

    private final AsyncJobSummaryRepository repository;
    private final AsyncJobType type;
    private final Class<PARAM> paramClass;
    private final Class<RES> resultClass;

    protected AsyncJobSummaryDto<PARAM, RES> create(String contextId, AsyncJobStatus status, PARAM params, RES result) {
        Objects.requireNonNull(contextId);
        Objects.requireNonNull(status);

        AsyncJobSummary saved = repository.saveAndFlush(AsyncJobSummary.builder()
            .contextId(contextId)
            .type(type)
            .status(status)
            .username(params.username())
            .parameters(toJson(params))
            .result(toJson(result))
            .build());
        return map(saved);
    }

    protected void update(Long id, AsyncJobStatus status, RES result) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(status);

        AsyncJobSummary entity = repository.findById(id).orElseThrow();
        entity.setResult(toJson(result));
        entity.setStatus(status);
        repository.saveAndFlush(entity);
    }

    protected Optional<AsyncJobSummaryDto<PARAM, RES>> getById(final Long id) {
        return repository
            .findByIdAndType(id, type)
            .map(this::map);
    }

    protected Optional<AsyncJobSummaryDto<PARAM, RES>> getLast(final String contextId) {
        return repository
            .findFirstByContextIdAndTypeOrderByCreatedDesc(contextId, type)
            .map(this::map);
    }

    protected Optional<AsyncJobSummaryDto<PARAM, RES>> getLast(final String contextId, final AsyncJobStatus status) {
        return repository
            .findFirstByContextIdAndTypeAndStatusOrderByCreatedDesc(contextId, type, status)
            .map(this::map);
    }

    protected List<AsyncJobSummaryDto<PARAM, RES>> getInProgress(final String contextId) {
        return getByContextIdAndStatusIn(contextId, AsyncJobStatus.IN_PROGRESS_STATUSES);
    }

    protected List<AsyncJobSummaryDto<PARAM, RES>> getByContextIdAndStatusIn(final String contextId, final Set<AsyncJobStatus> statuses) {
        return repository
            .findByContextIdAndTypeAndStatusIn(contextId, type, statuses)
            .stream()
            .map(this::map).toList();
    }

    protected AsyncJobSummaryDto<PARAM, RES> map(final AsyncJobSummary entity) {
        return AsyncJobSummaryConverter.convert(entity, paramClass, resultClass);
    }

    private String toJson(Object object) {
        return AsyncJobSummaryJsonMapper.toJson(object);
    }

}
