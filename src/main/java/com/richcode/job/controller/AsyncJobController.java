package com.richcode.job.controller;

import com.richcode.job.dto.rest.AsyncJobApiResponse;
import com.richcode.job.dto.rest.PostAsyncJobRequest;
import com.richcode.job.service.AsyncJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Async Job Processing")
@RestController
@RequestMapping("/v1/jobs")
@RequiredArgsConstructor
public class AsyncJobController {

    private final AsyncJobService service;

    @Operation(summary = "Trigger an async job")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public AsyncJobApiResponse run(@RequestBody PostAsyncJobRequest request) {
        service.run(request.getType(), request.getParameters());
        return AsyncJobApiResponse.ok();
    }

    @Operation(summary = "Re-trigger an async job")
    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public AsyncJobApiResponse rerun(@PathVariable("id") final Long id) {
        service.rerun(id);
        return AsyncJobApiResponse.ok();
    }

}
