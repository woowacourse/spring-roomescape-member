package roomescape.global;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReservationDateNullException;
import roomescape.reservation.domain.exception.ReservationTimeNullException;
import roomescape.reservation.domain.exception.ReserverNameEmptyException;

@RestControllerAdvice
public class RoomescapeExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNoSuchElementException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<String> handelPastReservationException(PastReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReserverNameEmptyException.class)
    public ResponseEntity<String> handelReserverNameEmptyException(ReserverNameEmptyException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReservationDateNullException.class)
    public ResponseEntity<String> handelReservationDateNullException(ReservationDateNullException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReservationTimeNullException.class)
    public ResponseEntity<String> handelReservationTimeNullException(ReservationTimeNullException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
