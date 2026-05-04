package roomescape.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandledException(Exception e) {
        return ResponseEntity.internalServerError().body("서버 내부에서 예상치 못한 문제가 발생했습니다. " + e.getMessage());
    }
}
