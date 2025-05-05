package roomescape.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.exception.DeletionNotAllowedException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<String> handleDeleteNotAllowedException(final DeletionNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
