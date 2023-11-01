package com.richcode.foo;

import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobSummaryDto;
import com.richcode.job.dto.AsyncJobType;
import com.richcode.job.dto.BasicAsyncJobResult;
import com.richcode.job.process.AsyncJobResult;
import com.richcode.job.repository.AsyncJobSummaryRepository;
import com.richcode.job.service.AbstractAsyncJobSummaryService;
import com.richcode.foo.dto.FooAsyncJobParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FooAsyncJobSummaryService extends AbstractAsyncJobSummaryService<FooAsyncJobParams, AsyncJobResult> {

    @Autowired
    public FooAsyncJobSummaryService(AsyncJobSummaryRepository repository) {
        super(repository, AsyncJobType.JOB_TYPE_1, FooAsyncJobParams.class, AsyncJobResult.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AsyncJobSummaryDto<FooAsyncJobParams, AsyncJobResult> queued(FooAsyncJobParams params) {
        return super.create(params.contextId(), AsyncJobStatus.QUEUED, params, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pending(Long jobId) {
        super.update(jobId, AsyncJobStatus.PENDING, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void success(Long jobId) {
        super.update(jobId, AsyncJobStatus.SUCCESS, BasicAsyncJobResult.completed());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failure(Long jobId, String msg) {
        super.update(jobId, AsyncJobStatus.FAILURE, BasicAsyncJobResult.failure(msg));
    }

}
