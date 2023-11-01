package com.richcode.foo;

import com.richcode.foo.dto.FooAsyncJobParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FooAsyncJobExecutor {

    private final FooAsyncJobRunner fooAsyncJobRunner;

    public Long run(FooAsyncJobParams params) {
        return fooAsyncJobRunner.run(params);
    }

}
