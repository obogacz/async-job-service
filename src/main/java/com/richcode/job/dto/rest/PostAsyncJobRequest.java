package com.richcode.job.dto.rest;

import com.richcode.job.dto.AsyncJobType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostAsyncJobRequest {

    private AsyncJobType type;
    private String parameters;

}
