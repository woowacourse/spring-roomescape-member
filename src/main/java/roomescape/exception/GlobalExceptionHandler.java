package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ErrorResponse> handle(RootException e) {
        logger.warn("Handled RootException: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.badRequest(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        logger.error("Handled Exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.internalServerError(e));
    }
}
