package com.richcode.job.dto.rest;

import com.richcode.job.dto.AsyncJobConfigurationDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAsyncJobConfigurationResponse {

    private AsyncJobConfigurationDto configuration;

}
