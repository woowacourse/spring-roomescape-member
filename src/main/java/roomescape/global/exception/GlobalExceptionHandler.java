package roomescape.global.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.global.exception.RoomEscapeException.AuthenticationException;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException e) {
        System.out.println("[ERROR] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException e) {
        System.out.println("[ERROR] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<String> handleDataAccessError() {
        System.out.println("[ERROR] " + "데이터베이스 접근에 실패하였습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 접근에 실패하였습니다.");
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationError(AuthenticationException e) {
        System.out.println("[ERROR] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleUnexpectedError(Exception e) {
        System.out.println("[ERROR] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
