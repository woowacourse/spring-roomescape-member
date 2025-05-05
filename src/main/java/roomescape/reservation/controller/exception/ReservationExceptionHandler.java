package roomescape.reservation.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReserverNameEmptyException;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(ReserverNameEmptyException.class)
    public ResponseEntity<Void> handleReserverNameEmptyException(ReserverNameEmptyException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<Void> handlePastReservationException(PastReservationException e) {
        return ResponseEntity.badRequest().build();
    }
}
