package roomescape.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import roomescape.exception.AuthenticationException;
import roomescape.exception.EntityExistsException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ForeignKeyViolationException;

@ControllerAdvice(basePackages = "roomescape.controller.rest")
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExistsException(EntityExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ForeignKeyViolationException.class)
    public ResponseEntity<String> handleForeignKeyViolationException(ForeignKeyViolationException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException(AuthenticationException ignored) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Void> handleExpiredJwtException(ExpiredJwtException ignored) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
