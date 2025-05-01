package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(value = ReservationException.class)
    public ResponseEntity<String> handleReservationException(ReservationException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(value = ReservationThemeException.class)
    public ResponseEntity<String> handleReservationThemeException(ReservationThemeException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(value = ReservationTimeException.class)
    public ResponseEntity<String> handleReservationTimeException(ReservationTimeException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }
}
