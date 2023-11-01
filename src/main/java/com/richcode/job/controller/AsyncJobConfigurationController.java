package com.richcode.job.controller;

import com.richcode.job.dto.rest.AsyncJobApiResponse;
import com.richcode.job.dto.rest.GetAsyncJobConfigurationResponse;
import com.richcode.job.dto.rest.PostAsyncJobConfigurationRequest;
import com.richcode.job.process.AsyncJobProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Async Job Configuration")
@RestController
@RequestMapping("/v1/jobs/configuration")
@RequiredArgsConstructor
class AsyncJobConfigurationController {

    private final AsyncJobProcessor asyncJobProcessor;

    @Operation(summary = "Get async jobs processor configuration")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public GetAsyncJobConfigurationResponse get() {
        return new GetAsyncJobConfigurationResponse(asyncJobProcessor.getConfiguration());
    }

    @Operation(summary = "Update async jobs processor configuration")
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public AsyncJobApiResponse put(@RequestBody final PostAsyncJobConfigurationRequest request) {
        asyncJobProcessor.updateConfiguration(request.getConfiguration());
        return AsyncJobApiResponse.ok();
    }

}
