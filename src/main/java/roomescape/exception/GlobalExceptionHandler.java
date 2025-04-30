package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Void> handleDataNotFoundException(final DataNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DataExistException.class)
    public ResponseEntity<Void> handDataExistException(final DataExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
