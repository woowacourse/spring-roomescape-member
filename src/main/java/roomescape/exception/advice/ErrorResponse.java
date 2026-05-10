package roomescape.exception.advice;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        Instant timestamp,
        String path,
        List<FieldError> errors
) {

    public static ErrorResponse of(String code, String message, String path) {
        return new ErrorResponse(code, message, Instant.now(), path, null);
    }

    public static ErrorResponse of(String code, String message, String path, List<FieldError> errors) {
        return new ErrorResponse(code, message, Instant.now(), path, errors);
    }

    public record FieldError(String field, String message) {
    }
}
