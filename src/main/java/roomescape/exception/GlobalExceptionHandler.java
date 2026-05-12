package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<?> handle(RoomescapeException exception) {
        ErrorCode code = exception.getErrorCode();
        return ResponseEntity
            .status(code.getStatus())
            .body(
                ErrorResponse.of(code.getMessage())
            );
    }
}
