package roomescape.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException() {
        return ResponseEntity.badRequest().body("잘못된 날짜/시간 형식입니다.");
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, NoSuchElementException.class})
    public ResponseEntity<String> handleIllegalException(final Exception e) {
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }
}
