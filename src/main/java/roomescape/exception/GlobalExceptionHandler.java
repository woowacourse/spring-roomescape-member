package roomescape.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ExceptionTemplate> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleAuthorizationException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionTemplate(exception.getMessage()));
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

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleNullPointerException(NullPointerException exception) {
        return ResponseEntity.badRequest().body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleDuplicateKeyException(DuplicateKeyException exception) {
        return ResponseEntity.badRequest().body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleExpiredJwtException(ExpiredJwtException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionTemplate(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ExceptionTemplate(exception.getMessage()));
    }
}
