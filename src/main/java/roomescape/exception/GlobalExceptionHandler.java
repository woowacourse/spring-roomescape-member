package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(final RoomescapeException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
