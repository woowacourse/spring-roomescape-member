package roomescape.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.response.ErrorResponse;
import roomescape.exception.custom.RoomescapeException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(exception.httpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
