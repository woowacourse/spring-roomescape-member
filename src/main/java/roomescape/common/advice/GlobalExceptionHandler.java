package roomescape.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandledException(Exception e) {
        return ResponseEntity.internalServerError().body("서버 내부에서 예상치 못한 문제가 발생했습니다. " + e.getMessage());
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleValidationException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 파라미터가 누락되었거나 형식이 잘못되었습니다." + e.getMessage());
    }
}
