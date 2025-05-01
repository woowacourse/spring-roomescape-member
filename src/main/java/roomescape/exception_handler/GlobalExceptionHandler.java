package roomescape.exception_handler;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.ConstraintException;
import roomescape.exception.ResourceNotExistException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<String> handleConstraintException(ConstraintException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotExistException.class)
    public ResponseEntity<Void> handleResourceNotExistException(ResourceNotExistException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Void> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.notFound().build();
    }
}
