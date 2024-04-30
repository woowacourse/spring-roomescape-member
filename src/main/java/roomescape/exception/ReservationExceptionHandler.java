package roomescape.exception;

import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException exception) {
        return new ResponseEntity<>("올바르지 않은 시간/날짜 형식입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
