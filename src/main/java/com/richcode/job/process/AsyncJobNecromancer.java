package com.richcode.job.process;

import com.richcode.job.converter.AsyncJobSummaryConverter;
import com.richcode.job.dto.AsyncJobSummaryRoughDto;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.job.register.AsyncJobRegister;
import com.richcode.job.repository.AsyncJobSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.richcode.job.dto.AsyncJobStatus.IN_PROGRESS_STATUSES;
import static java.util.stream.Collectors.*;

@Slf4j
@Component
@RequiredArgsConstructor
class AsyncJobNecromancer {

    private final AsyncJobSummaryRepository repository;
    private final AsyncJobRegister asyncJobRegister;

    @EventListener(ApplicationReadyEvent.class)
    public void resurrectAsyncJobs() {
        log.info("Checking if there are jobs to be resurrected");

        final List<AsyncJobSummaryRoughDto> jobsInProgress = findSummariesInProgress();
        log.info("Found {} jobs to resurrect", jobsInProgress.size());

        delete(jobsInProgress);
        run(jobsInProgress);
    }

    public List<AsyncJobSummaryRoughDto> findSummariesInProgress() {
        return repository.findByStatusIn(IN_PROGRESS_STATUSES).stream()
            .map(AsyncJobSummaryConverter::convert)
            .collect(toList());
    }

    private void delete(final List<AsyncJobSummaryRoughDto> jobs) {
        repository.deleteAllById(jobs.stream()
            .map(AsyncJobSummaryRoughDto::id)
            .collect(toSet()));
    }

    private void run(final List<AsyncJobSummaryRoughDto> jobs) {
        jobs.stream()
            .collect(groupingBy(
                AsyncJobSummaryRoughDto::type,
                mapping(AsyncJobSummaryRoughDto::parameters, toList())))
            .forEach(this::run);
    }

    private void run(final AsyncJobType type, final List<String> parameters) {
        parameters.forEach(p -> run(type, p));
    }

    private void run(final AsyncJobType type, final String parametersJson) {
        log.info("Resurrecting {} job with params: {}", type, parametersJson);
        asyncJobRegister.getJobRunner(type).forEach(runner -> runner.run(parametersJson));
    }

}
