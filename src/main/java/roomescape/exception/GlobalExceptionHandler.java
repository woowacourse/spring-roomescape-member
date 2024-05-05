package roomescape.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = JsonMappingException.class)
    public ResponseEntity<String> handleBadRequestException(JsonMappingException ex) {
        return ResponseEntity.badRequest().body("[ERROR] 적절하지 않은 입력값 입니다");
    }
}
