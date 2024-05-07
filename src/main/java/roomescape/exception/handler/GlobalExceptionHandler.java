package roomescape.exception.handler;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import roomescape.exception.DuplicatedDataException;
import roomescape.exception.EmptyDataAccessException;
import roomescape.exception.InvalidNameException;
import roomescape.exception.PastDateReservationException;
import roomescape.exception.PastTimeReservationException;
import roomescape.exception.UnableDeleteDataException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicatedDataException.class)
    public ResponseEntity<String> handleDuplicateReservation(DuplicatedDataException ex) {
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

    @ExceptionHandler(value = UnableDeleteDataException.class)
    public ResponseEntity<String> handleUnableDeleteData(UnableDeleteDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("field : " + ex.getBindingResult().getFieldErrors().get(0)
                                                  .getField() + System.lineSeparator()
                                     + "message : " + ex.getBindingResult().getFieldErrors().get(0)
                                                        .getDefaultMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
