package com.richcode.job.controller;

import com.richcode.job.dto.rest.GetAsyncJobSummaryResponse;
import com.richcode.job.dto.rest.GetListAsyncJobSummaryRequest;
import com.richcode.job.dto.rest.GetListAsyncJobSummaryResponse;
import com.richcode.job.service.AsyncJobSummaryFinder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.richcode.job.repository.AsyncJobSummarySearchCriteriaRepository.AsyncJobSummarySearchCriteria;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Async Job Summary")
@RestController
@RequestMapping("/v1/jobs")
@RequiredArgsConstructor
class AsyncJobSummaryController {

    private final AsyncJobSummaryFinder finder;

    @Operation(summary = "Get async job summary")
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public GetAsyncJobSummaryResponse get(@PathVariable("id") Long id) {
        return new GetAsyncJobSummaryResponse(finder.find(id));
    }

    @Operation(summary = "Get async job summaries list")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public GetListAsyncJobSummaryResponse list(final GetListAsyncJobSummaryRequest request) {
        return new GetListAsyncJobSummaryResponse(finder.find(AsyncJobSummarySearchCriteria.builder()
            .idIn(request.getIds())
            .contextIdIn(request.getContextIds())
            .typeIn(request.getTypes())
            .statusIn(request.getStatuses())
            .page(request.toPageable())
            .build()));
    }

}
