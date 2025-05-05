package roomescape.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AlreadyInUseException.class)
    public ResponseEntity<Void> handleAlreadyUseException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
