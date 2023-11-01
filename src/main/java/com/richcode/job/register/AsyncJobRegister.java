package com.richcode.job.register;

import com.richcode.job.dto.AsyncJobType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncJobRegister {

    private final static List<AsyncJobRunnable> EMPTY_RUNNERS_LIST = List.of();

    private final ApplicationContext context;
    private final MultiValueMap<AsyncJobType, AsyncJobRunnable> jobRunners = new LinkedMultiValueMap<>();

    @PostConstruct
    public void init() {
        context.getBeansWithAnnotation(AsyncJobRunner.class).forEach(this::addRunner);
    }

    private void addRunner(String beanName, Object bean) {
        if (!(bean instanceof AsyncJobRunnable)) {
            throw new IllegalStateException(beanName + " is signed as a AsyncJobRunner but does not implement AsyncJobRunnable interface");
        }
        jobRunners.add(findValue(beanName), (AsyncJobRunnable) bean);
        log.info("Registered async job runner - {}", beanName);
    }

    private AsyncJobType findValue(String beanName) {
        return Optional
            .ofNullable(context.findAnnotationOnBean(beanName, AsyncJobRunner.class))
            .map(AsyncJobRunner::value)
            .orElseThrow();
    }

    public List<AsyncJobRunnable> getJobRunner(final AsyncJobType type) {
        return jobRunners.getOrDefault(type, EMPTY_RUNNERS_LIST);
    }
}
