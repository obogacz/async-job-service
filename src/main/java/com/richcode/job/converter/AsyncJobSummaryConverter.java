package com.richcode.job.converter;

import com.richcode.job.domain.AsyncJobSummary;
import com.richcode.job.dto.AsyncJobSummaryDto;
import com.richcode.job.dto.AsyncJobSummaryRoughDto;
import com.richcode.job.process.AsyncJobParams;
import com.richcode.job.process.AsyncJobResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncJobSummaryConverter {

    public static AsyncJobSummaryRoughDto convert(@NonNull final AsyncJobSummary job) {
        return AsyncJobSummaryRoughDto.builder()
            .id(job.getId())
            .type(job.getType())
            .status(job.getStatus())
            .contextId(job.getContextId())
            .username(job.getUsername())
            .parameters(job.getParameters())
            .result(job.getResult())
            .created(job.getCreated())
            .updated(job.getUpdated())
            .duration(job.getDurationTime())
            .build();
    }

    public static List<AsyncJobSummaryRoughDto> convert(@NonNull final List<AsyncJobSummary> jobs) {
        return jobs.stream().map(AsyncJobSummaryConverter::convert).collect(Collectors.toList());
    }

    public static <PARAM extends AsyncJobParams, RES extends AsyncJobResult> AsyncJobSummaryDto<PARAM, RES> convert(
        AsyncJobSummary entity,
        Class<PARAM> targetParamClass,
        Class<RES> targetResultClass
    ) {
        return AsyncJobSummaryDto.<PARAM, RES>builder()
            .id(entity.getId())
            .type(entity.getType())
            .status(entity.getStatus())
            .contextId(entity.getContextId())
            .username(entity.getUsername())
            .parameters(AsyncJobSummaryJsonMapper.fromJson(entity.getParameters(), targetParamClass))
            .result(AsyncJobSummaryJsonMapper.fromJson(entity.getResult(), targetResultClass))
            .created(entity.getCreated())
            .updated(entity.getUpdated())
            .duration(entity.getDurationTime())
            .build();
    }

}
