package roomescape.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleException(BadRequestException badRequestException) {
        badRequestException.printStackTrace();
        return ResponseEntity.badRequest().body(badRequestException.getMessage());
    }
}
