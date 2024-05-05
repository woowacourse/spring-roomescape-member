package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> handleRuntimeException() {
        return ResponseEntity.internalServerError()
                .body("서버에서 예기치 못한 오류가 발생했습니다. 문제가 지속되는 경우 관리자에게 문의해주세요.");
    }
}
