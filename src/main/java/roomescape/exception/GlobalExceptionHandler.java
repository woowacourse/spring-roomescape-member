package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ReservationException.class)
    public ResponseEntity<String> handleReservationException(ReservationException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException exception) {
        return ResponseEntity
                .status(401)
                .body(exception.getMessage());
    }

    @ExceptionHandler(value = ReservationThemeException.class)
    public ResponseEntity<String> handleReservationThemeException(ReservationThemeException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(value = ReservationTimeException.class)
    public ResponseEntity<String> handleReservationTimeException(ReservationTimeException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(value = MemberException.class)
    public ResponseEntity<String> handleMemberException(MemberException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception) {
        LOG.error("Unexpected error occurred", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 문제가 발생했습니다.");
    }
}
