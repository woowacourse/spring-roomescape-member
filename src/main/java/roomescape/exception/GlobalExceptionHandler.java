package roomescape.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return createBadRequestResponse(ex);
    }

    @ExceptionHandler(ReservationDuplicateException.class)
    public ResponseEntity<FailureResponse> handleReservationDuplicateException(ReservationDuplicateException ex) {
        return createBadRequestResponse(ex);
    }

    @ExceptionHandler(ReservationExistException.class)
    public ResponseEntity<FailureResponse> handleReservationExistException(ReservationExistException ex) {
        return createBadRequestResponse(ex);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<FailureResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return createBadRequestResponse(ex);
    }

    private ResponseEntity<FailureResponse> createBadRequestResponse(RuntimeException ex) {
        return new ResponseEntity<>(
                new FailureResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST
        );
    }
}
