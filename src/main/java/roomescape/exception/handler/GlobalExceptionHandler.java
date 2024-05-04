package roomescape.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EmptyDataAccessException;
import roomescape.exception.InvalidNameException;
import roomescape.exception.PastDateReservationException;
import roomescape.exception.PastTimeReservationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicateReservationException.class)
    public ResponseEntity<String> handleDuplicateReservation(DuplicateReservationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidNameException.class)
    public ResponseEntity<String> handleInvalidName(InvalidNameException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PastDateReservationException.class)
    public ResponseEntity<String> handlePastDateReservation(PastDateReservationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PastTimeReservationException.class)
    public ResponseEntity<String> handlePastTimeReservation(PastTimeReservationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmptyDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultDataAccess(EmptyDataAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
    }
}
