package roomescape.global.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.global.exception.AuthorizedException;
import roomescape.global.exception.DuplicateException;
import roomescape.global.exception.InvalidReservationException;
import roomescape.global.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<String> handleInvalidReservation(InvalidReservationException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(AuthorizedException.class)
    public ResponseEntity<String> handleAuthorized(AuthorizedException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicate(DuplicateException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }
}
