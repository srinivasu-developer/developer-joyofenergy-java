package uk.tw.energy.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

/**
 * @Author: srinivasun
 * @Since: 04/11/24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(Instant time, List<ErrorMessage> errors, String message) {

    public ErrorResponse(Instant time, String message) {
        this(time, null, message);
    }

    public ErrorResponse(Instant time, List<ErrorMessage> errors) {
        this(time, errors, null);
    }

    public record ErrorMessage(String fieldName, String message) {}
}
