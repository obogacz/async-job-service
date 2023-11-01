package com.richcode.foo;

import com.richcode.job.converter.AsyncJobSummaryJsonMapper;
import com.richcode.job.dto.AsyncJobSummaryDto;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.job.process.AsyncJobProcessor;
import com.richcode.job.process.AsyncJobResult;
import com.richcode.job.register.AsyncJobRunnable;
import com.richcode.job.register.AsyncJobRunner;
import com.richcode.foo.dto.FooAsyncJobParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@AsyncJobRunner(AsyncJobType.JOB_TYPE_1)
public class FooAsyncJobRunner implements AsyncJobRunnable {

    private final AsyncJobProcessor processor;
    private final FooAsyncJobSummaryService summaryService;

    @Override
    public Long run(String parametersJson) {
        return run(AsyncJobSummaryJsonMapper.fromJson(parametersJson, FooAsyncJobParams.class));
    }

    public Long run(FooAsyncJobParams params) {
        final AsyncJobSummaryDto<FooAsyncJobParams, AsyncJobResult> job = summaryService.queued(params);
        try {
            processor.submit(() -> process(job));
        } catch (Throwable throwable) {
            summaryService.failure(job.id(), throwable.getMessage());
            log.error("[ jobId={} ] Job failed due to: {}", job.id(), throwable.getMessage());
        }
        return job.id();
    }

    private void process(AsyncJobSummaryDto<FooAsyncJobParams, AsyncJobResult> job) {
        log.info("Started processing job [id={}, type={}, contextId={}]", job.id(), job.type(), job.contextId());
        try {
            summaryService.pending(job.id());

            Thread.sleep(10_000L); // logic to process

            summaryService.success(job.id());
            log.info("Finished processing job [id={}, type={}, contextId={}]", job.id(), job.type(), job.contextId());
        } catch (Throwable throwable) {
            summaryService.failure(job.id(), throwable.getMessage());
            log.error("Job failed due to: {} [id={}, type={}, contextId={}]",
                throwable.getMessage(), job.id(), job.type(), job.contextId());
        }
    }

}
