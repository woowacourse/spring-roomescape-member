package roomescape.exceptionhandler;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.AuthenticationException;
import roomescape.exception.ConstraintException;
import roomescape.exception.ResourceNotExistException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NullPointerException.class, ConstraintException.class,
        IllegalArgumentException.class, AuthenticationException.class})
    public ResponseEntity<String> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotExistException.class)
    public ResponseEntity<Void> handleResourceNotExistException(ResourceNotExistException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Void> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.internalServerError().build();
    }
}
