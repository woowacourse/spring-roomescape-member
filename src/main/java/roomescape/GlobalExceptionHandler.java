package roomescape;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.DuplicatePlayTimeException;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PlayTimeNotFoundException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ThemeNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ReservationNotFoundException.class, PlayTimeNotFoundException.class, ThemeNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(final NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {DuplicateReservationException.class, DuplicatePlayTimeException.class})
    public ResponseEntity<Void> handleDuplicateException(final IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
