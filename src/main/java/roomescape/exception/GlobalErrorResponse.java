package roomescape.exception;

import java.util.List;

public record GlobalErrorResponse(
        String message,
        List<ErrorDetail> errors
) {

    public static GlobalErrorResponse from(String message) {
        return new GlobalErrorResponse(message, List.of());
    }

    public static GlobalErrorResponse of(String message, List<ErrorDetail> errors) {
        return new GlobalErrorResponse(message, errors);
    }
}
