package roomescape.presentation;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.DuplicatePlayTimeException;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidReservationDateException;
import roomescape.exception.PlayTimeNotFoundException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.UserNotFoundException;
import roomescape.exception.auth.InvalidCredentialsException;
import roomescape.exception.auth.InvalidTokenException;
import roomescape.exception.auth.UnauthorizedAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {InvalidCredentialsException.class, InvalidTokenException.class})
    public ResponseEntity<Void> handleAuthenticationFailure(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = UnauthorizedAccessException.class)
    public ResponseEntity<Void> handleAuthorizationFailure(final UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(value = {
            ReservationNotFoundException.class,
            PlayTimeNotFoundException.class,
            ThemeNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<Void> handleNotFoundException(final NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {DuplicateReservationException.class, DuplicatePlayTimeException.class})
    public ResponseEntity<Void> handleDuplicateException(final IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(value = InvalidReservationDateException.class)
    public ResponseEntity<Void> handleInvalidReservationDateException(final InvalidReservationDateException e) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
