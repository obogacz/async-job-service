package com.richcode.job.dto.rest;

import com.richcode.job.dto.AsyncJobSummaryRoughDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAsyncJobSummaryResponse {

    private AsyncJobSummaryRoughDto summary;

}
