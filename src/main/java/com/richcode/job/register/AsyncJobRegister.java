package com.richcode.job.register;

import com.richcode.job.dto.AsyncJobType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AsyncJobRegister {

    private final static List<AsyncJobRunnable> EMPTY_RUNNERS_LIST = List.of();
    private final MultiValueMap<AsyncJobType, AsyncJobRunnable> runners = new LinkedMultiValueMap<>();

    @Autowired
    public AsyncJobRegister(Collection<AsyncJobRunnable> runners) {
        runners.forEach(this::addRunner);
    }

    private void addRunner(final AsyncJobRunnable runner) {
        Optional.ofNullable(AnnotationUtils.findAnnotation(runner.getClass(), AsyncJobRunner.class))
            .map(AsyncJobRunner::value)
            .ifPresentOrElse(
                type -> addRunner(type, runner),
                () -> log.warn("Registration of Async Job Runner {} failed: missing @AsyncJobRunner", runner.getClass().getName())
            );
    }

    private void addRunner(final AsyncJobType type, final AsyncJobRunnable runner) {
        runners.add(type, runner);
        log.info("Registered Async Job Runner: {} - {}", type, runner.getClass().getName());
    }

    public synchronized List<AsyncJobRunnable> getJobRunner(final AsyncJobType type) {
        return List.copyOf(runners.getOrDefault(type, EMPTY_RUNNERS_LIST));
    }
}
