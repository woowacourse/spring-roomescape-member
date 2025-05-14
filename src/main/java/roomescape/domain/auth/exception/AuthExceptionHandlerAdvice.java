package roomescape.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandlerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundForTokenException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("유효하지 않은 계정입니다.");
    }

    @ExceptionHandler(InvalidAuthorizationException.class)
    public ResponseEntity<String> handleInvalidAuthorizationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("유효하지 않은 인증입니다.");
    }
}
