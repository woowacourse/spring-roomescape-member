package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Void> handleReservationNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<Void> handleReservationTimeNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ReservationTimeInUseException.class)
    public ResponseEntity<Void> handleReservationTimeInUse() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    public ResponseEntity<Void> handleReservationAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<Void> handleThemeNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedException(Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}
