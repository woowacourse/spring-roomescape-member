package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ErrorResponse> handle(RootException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.badRequest(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        return ResponseEntity.internalServerError().body(ErrorResponse.internalServerError(e));
    }
}
