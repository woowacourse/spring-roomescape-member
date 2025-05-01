package roomescape.reservation.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReserverNameEmptyException;

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
