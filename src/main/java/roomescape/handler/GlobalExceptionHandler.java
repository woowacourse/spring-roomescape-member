package roomescape.handler;

import java.time.DateTimeException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            NullPointerException.class,
            DateTimeException.class,
            IllegalArgumentException.class,
            IncorrectResultSizeDataAccessException.class})
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }
}
