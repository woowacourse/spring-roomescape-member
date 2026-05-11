package roomescape.support.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        return ErrorResponse.of(errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ErrorResponse.of(RoomescapeErrorCode.INTERNAL_SERVER_ERROR);
    }
}
