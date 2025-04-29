package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleMethodNotAllowed(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
