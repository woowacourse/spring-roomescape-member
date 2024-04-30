package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<String> handleCustomException(NotFoundTimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException() {
        return new ResponseEntity<>("서버 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
