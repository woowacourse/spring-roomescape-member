package roomescape.time.presentation.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.time.domain.exception.ReservationTimeInUseException;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ReservationTimeExceptionHandler {

    @ExceptionHandler(ReservationTimeInUseException.class)
    public ResponseEntity<String> handleReservationTimeInUseException(ReservationTimeInUseException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<String> handleReservationTimeNotFoundException(ReservationTimeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
