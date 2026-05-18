package roomescape.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.response.ErrorResponse;
import roomescape.exception.custom.RoomescapeException;

@RestControllerAdvice
@Order
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(exception.httpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 내부 오류가 발생했습니다."));
    }
}
