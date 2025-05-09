package roomescape.exception;

import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestApiControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        return createFailureResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationDuplicateException.class)
    public ResponseEntity<FailureResponse> handleReservationDuplicateException(final ReservationDuplicateException ex) {
        return createFailureResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationExistException.class)
    public ResponseEntity<FailureResponse> handleReservationExistException(final ReservationExistException ex) {
        return createFailureResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<FailureResponse> handleNoSuchElementException(final NoSuchElementException ex) {
        return createFailureResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<FailureResponse> handleJsonParseException(final DateTimeParseException ex) {
        return createFailureResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException ex) {
        return createFailureResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<FailureResponse> handleException(final Exception ex) {
        return createFailureResponse(new RuntimeException("예기치 못한 오류가 발생했습니다."), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<FailureResponse> createFailureResponse(final Exception ex, final HttpStatus status) {
        return new ResponseEntity<>(new FailureResponse(status, ex.getMessage()), status);
    }
}
