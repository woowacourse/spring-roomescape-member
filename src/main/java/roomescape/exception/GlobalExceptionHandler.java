package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// TODO 예상치 못한 예외 처리 고민
//  TODO IllegalStateException, RuntimeException 과 같은 도메인 오류 처리
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handle(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }
}
