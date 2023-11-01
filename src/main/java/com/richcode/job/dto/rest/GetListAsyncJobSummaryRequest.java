package com.richcode.job.dto.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static org.springframework.data.domain.Sort.Direction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListAsyncJobSummaryRequest {

    @Schema(description = "Async job summary ids")
    private Set<Long> ids;

    @Schema(description = "Async job context ids")
    private Set<String> contextIds;

    @Schema(description = "Async job types")
    private Set<AsyncJobType> types;

    @Schema(description = "Async job statuses")
    private Set<AsyncJobStatus> statuses;

    @Schema(description = "Async jobs page")
    private Page page;

    @JsonIgnore
    public Pageable toPageable() {
        return page != null ? page.toPageable() : new Page().toPageable();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page {
        private Integer pageNumber = 0;
        private Integer pageSize = 16;
        private Direction direction = Direction.DESC;
        private String[] properties = { "id" };

        @JsonIgnore
        public Pageable toPageable() {
            return PageRequest.of(pageNumber, pageSize, direction, properties);
        }
    }

}
