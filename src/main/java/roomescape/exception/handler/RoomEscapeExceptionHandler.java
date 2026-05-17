package roomescape.exception.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.RoomEscapeErrorCodeHttpStatusMapper;
import roomescape.exception.RoomEscapeException;
import roomescape.exception.dto.ErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class RoomEscapeExceptionHandler {

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomEscapeException(RoomEscapeException e) {
        return ResponseEntity
                .status(RoomEscapeErrorCodeHttpStatusMapper.getHttpStatus(e.getErrorCode()))
                .body(ErrorResponse.of(e.getErrorCode()));
    }

}
