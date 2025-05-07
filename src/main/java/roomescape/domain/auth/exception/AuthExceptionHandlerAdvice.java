package roomescape.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandlerAdvice {

    @ExceptionHandler(UserNotFoundForTokenException.class)
    public ResponseEntity<String> handleUserNotFoundForTokenException(final UserNotFoundForTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("유효하지 않은 계정입니다.");
    }
}
