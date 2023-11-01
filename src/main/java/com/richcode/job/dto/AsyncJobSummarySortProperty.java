package com.richcode.job.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//public enum AsyncJobSummarySortProperty implements SortBy {
public enum AsyncJobSummarySortProperty {

    ID("id"),
    CREATED("created"),
    UPDATED("updated");

    private final String propertyName;

//    @Override
//    public String getPropertyName() {
//        return propertyName;
//    }
}
