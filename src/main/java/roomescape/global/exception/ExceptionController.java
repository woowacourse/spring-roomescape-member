package roomescape.global.exception;

import java.time.format.DateTimeParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException() {
        return ResponseEntity.badRequest().body("시간 형식이 잘못되었습니다.");
    }
}
