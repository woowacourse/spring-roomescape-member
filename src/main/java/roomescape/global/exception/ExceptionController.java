package roomescape.global.exception;

import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    private static final String PREFIX = "[ERROR] ";
    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException() {
        return ResponseEntity.badRequest().body(PREFIX + "시간 형식이 잘못되었습니다.");
    }

    @ExceptionHandler(DuplicateTimeException.class)
    public ResponseEntity<String> handleDuplicateTimeException(DuplicateTimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({GetTimeException.class, GetThemeException.class})
    public ResponseEntity<String> handleGetTimeException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({DeleteTimeException.class, DeleteReservationException.class, DeleteThemeException.class})
    public ResponseEntity<String> handleDeleteException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PastTimeException.class)
    public ResponseEntity<String> handlePastTimeException(PastTimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<String> handleDuplicateReservationException(DuplicateReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(PREFIX + e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleBasicException(Exception e) {
        log.error("error has occurred ) {}", e.getMessage());
        log.error("error class ) {}", e.getClass());
        return ResponseEntity.internalServerError().body(PREFIX + " 알 수 없는 에러가 발생했습니다.");
    }
}
