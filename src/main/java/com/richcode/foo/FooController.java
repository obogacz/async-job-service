package com.richcode.foo;

import com.richcode.foo.dto.FooAsyncJobParams;
import com.richcode.job.dto.rest.AsyncJobApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Foo")
@RestController
@RequestMapping("/v1/foo")
@RequiredArgsConstructor
public class FooController {

    private final FooAsyncJobExecutor executor;

    @Operation(summary = "Run a foo async job")
    @PostMapping(value = "/run", produces = APPLICATION_JSON_VALUE)
    public AsyncJobApiResponse rerun() {
        executor.run(FooAsyncJobParams.builder()
            .contextId("1")
            .username("me")
            .fooParam("foo")
            .build());
        return AsyncJobApiResponse.ok();
    }

}
