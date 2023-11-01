package com.richcode.job.service;

import com.richcode.job.converter.AsyncJobSummaryConverter;
import com.richcode.job.dto.AsyncJobSummaryRoughDto;
import com.richcode.job.repository.AsyncJobSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

import static com.richcode.job.repository.AsyncJobSummarySearchCriteriaRepository.AsyncJobSummarySearchCriteria;

@Component
@RequiredArgsConstructor
public class AsyncJobSummaryFinder {

    private final AsyncJobSummaryRepository repository;

    public AsyncJobSummaryRoughDto find(final long id) {
        return repository.findById(id)
            .map(AsyncJobSummaryConverter::convert)
            .orElseThrow(() -> new NoSuchElementException("Job summary with id=" + id + " does not exist"));
    }

    public Page<AsyncJobSummaryRoughDto> find(final AsyncJobSummarySearchCriteria searchCriteria) {
        return repository.findPage(searchCriteria).map(AsyncJobSummaryConverter::convert);
    }

}
