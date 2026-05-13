package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_PREFIX = "[ERROR] ";

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ERROR_PREFIX + "존재하지 않는 예약입니다."));
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ERROR_PREFIX + "존재하지 않는 예약시간입니다."));
    }

    @ExceptionHandler(ReservationTimeInUseException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeInUse() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ERROR_PREFIX + "예약이 존재하는 예약시간은 삭제할 수 없습니다."));
    }

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleReservationAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ERROR_PREFIX + "이미 예약이 존재합니다."));
    }

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleThemeNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ERROR_PREFIX + "존재하지 않는 테마입니다."));
    }

    @ExceptionHandler(ThemeInUseException.class)
    public ResponseEntity<ErrorResponse> handleThemeInUse() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ERROR_PREFIX + "예약이 존재하는 테마는 삭제할 수 없습니다."));
    }

    @ExceptionHandler(PastReservationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handlePastReservation() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ERROR_PREFIX + "지나간 날짜와 시간으로는 예약할 수 없습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ERROR_PREFIX + "예기치 못한 예외가 발생했습니다."));
    }
}
