package com.richcode.job.service;

import com.richcode.job.dto.AsyncJobSummaryRoughDto;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.job.register.AsyncJobRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncJobService {

    private final AsyncJobSummaryFinder finder;
    private final AsyncJobRegister asyncJobRegister;

    public AsyncJobSummaryRoughDto find(final long id) {
        return finder.find(id);
    }

    @Transactional
    public void run(final AsyncJobType type, final String parametersJson) {
        asyncJobRegister.getJobRunner(type).forEach(runner -> runner.run(parametersJson));
        log.info("Triggered {} job with params: {}", type, parametersJson);
    }

    @Transactional
    public void rerun(final Long id) {
        final AsyncJobSummaryRoughDto job = find(id);
        asyncJobRegister.getJobRunner(job.type()).forEach(runner -> runner.run(job.parameters()));
        log.info("Re-triggered {} job with id={} and params: {}", job.type(), job.id(), job.parameters());
    }

}
