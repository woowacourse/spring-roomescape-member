package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalUserRequestException.class)
    ResponseEntity<String> handleIllegalUserRequestException(IllegalUserRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
