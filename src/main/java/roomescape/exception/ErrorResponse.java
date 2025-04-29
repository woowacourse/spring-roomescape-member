package roomescape.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        String exceptionType,
        String message
) {
    public static ErrorResponse badRequest(Exception e) {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }

    public static ErrorResponse internalServerError(Exception e) {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }
}
