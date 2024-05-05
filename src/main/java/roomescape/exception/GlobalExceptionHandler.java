package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    protected ResponseEntity<ExceptionTemplate> handleInvalidReservationException(
            final InvalidReservationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler
    protected ResponseEntity<ExceptionTemplate> handleValidationException(
            final MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(new ExceptionTemplate(message));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleValidationException(HandlerMethodValidationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionTemplate> handleNullPointerException(NullPointerException exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionTemplate> handleException(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionTemplate(exception.getMessage()));
    }
}
