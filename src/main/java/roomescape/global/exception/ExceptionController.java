package roomescape.global.exception;

import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException() {
        return ResponseEntity.badRequest().body("시간 형식이 잘못되었습니다.");
    }

    @ExceptionHandler(DuplicateTimeException.class)
    public ResponseEntity<String> handleDuplicateTimeException(DuplicateTimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(GetTimeException.class)
    public ResponseEntity<String> handleGetTimeException(GetTimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({DeleteTimeException.class, DeleteReservationException.class})
    public ResponseEntity<String> handleDeleteException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body("[ERROR] " + e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }
}
