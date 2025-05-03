package roomescape.presentation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.domain.exception.PastReservationException;
import roomescape.domain.exception.ReserverNameEmptyException;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(ReserverNameEmptyException.class)
    public ResponseEntity<String> handleReserverNameEmptyException(ReserverNameEmptyException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<String> handlePastReservationException(PastReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
