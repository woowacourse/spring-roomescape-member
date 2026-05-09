package roomescape.exception.response;

import java.util.List;

public record ErrorResponse(
        String message,
        List<String> validationErrors
) {
    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, null);
    }

    public static ErrorResponse of(String message, List<String> errors) {
        return new ErrorResponse(message, errors);
    }
}
