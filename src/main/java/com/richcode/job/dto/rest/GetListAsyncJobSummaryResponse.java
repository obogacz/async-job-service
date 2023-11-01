package com.richcode.job.dto.rest;

import com.richcode.job.dto.AsyncJobSummaryRoughDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class GetListAsyncJobSummaryResponse {

    private List<AsyncJobSummaryRoughDto> summaries;

    public GetListAsyncJobSummaryResponse(Page<AsyncJobSummaryRoughDto> page) {
        this.summaries = page.getContent();
    }

}
