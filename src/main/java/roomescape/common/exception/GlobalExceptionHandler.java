package roomescape.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}
