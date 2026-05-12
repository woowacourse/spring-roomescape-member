package roomescape.handler;

import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.CannotDeleteReservationTimeException;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.EmptyNameException;
import roomescape.exception.ReservationByPastDateTimeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ReservationByPastDateTimeException.class,
            EmptyNameException.class
    })
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(CannotDeleteReservationTimeException.class)
    public ResponseEntity<String> handleCannotDeleteReservationTimeException(CannotDeleteReservationTimeException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicatedReservationException.class)
    public ResponseEntity<String> handleDuplicatedReservationException(DuplicatedReservationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(ex.getMessage());
    }
}
