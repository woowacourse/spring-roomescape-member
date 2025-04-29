package roomescape.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException() {
        return ResponseEntity.badRequest().body("잘못된 날짜/시간 형식입니다.");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException e) {
        String message = e.getMessage();
        if (message == null) {
            message = "잘못된 입력입니다.";
        }
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(final IllegalStateException e) {
        String message = e.getMessage();
        if (message == null) {
            message = "실행할 수 없는 상태입니다.";
        }
        return ResponseEntity.badRequest().body(message);
    }
}
