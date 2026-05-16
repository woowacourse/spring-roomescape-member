package roomescape.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.RoomEscapeErrorCodeHttpStatusMapper;
import roomescape.exception.RoomEscapeException;
import roomescape.exception.dto.ErrorResponse;

@RestControllerAdvice(annotations = RestController.class)
public class RoomEscapeExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRoomEscapeException(RoomEscapeException e) {
        return ResponseEntity
                .status(RoomEscapeErrorCodeHttpStatusMapper.getHttpStatus(e.getErrorCode()))
                .body(ErrorResponse.of(e.getErrorCode()));
    }

}
