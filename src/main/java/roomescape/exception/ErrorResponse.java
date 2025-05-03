package roomescape.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        String exceptionType,
        String message,
        List<FieldErrorResponse> fieldErrorResponses
) {
    public static ErrorResponse badRequest(Exception e) {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(),
                e.getMessage(),
                null
        );
    }

    public static ErrorResponse badRequest(Exception e, List<FieldErrorResponse> fieldErrorResponses) {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(),
                "Validation failed",
                fieldErrorResponses
        );
    }

    public static ErrorResponse internalServerError(Exception e) {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getClass().getSimpleName(),
                e.getMessage(),
                null
        );
    }
}
