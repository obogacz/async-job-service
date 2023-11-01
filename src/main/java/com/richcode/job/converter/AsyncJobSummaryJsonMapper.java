package com.richcode.job.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncJobSummaryJsonMapper {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> target) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON_MAPPER.readValue(json, target);
    }

    @SneakyThrows
    public static String toJson(Object object) {
        if (isNull(object)) {
            return null;
        }
        return JSON_MAPPER.writeValueAsString(object);
    }

}
