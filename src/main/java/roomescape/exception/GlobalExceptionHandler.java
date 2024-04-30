package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    protected ResponseEntity<ExceptionTemplate> handleInvalidReservationException(
            final InvalidReservationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionTemplate(exception.getMessage()));
    }
}
