package roomescape.exception;

import io.swagger.v3.oas.annotations.Hidden;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return createFailureResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationDuplicateException.class)
    public ResponseEntity<FailureResponse> handleReservationDuplicateException(ReservationDuplicateException ex) {
        return createFailureResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationExistException.class)
    public ResponseEntity<FailureResponse> handleReservationExistException(ReservationExistException ex) {
        return createFailureResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<FailureResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return createFailureResponse(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<FailureResponse> createFailureResponse(final RuntimeException ex, HttpStatus status) {
        return new ResponseEntity<>(new FailureResponse(status, ex.getMessage()), status);
    }
}
