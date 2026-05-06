package roomescape.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import roomescape.holiday.exception.HolidayNotFoundException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.time.exception.TimeNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TimeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTimeNotFound(TimeNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of(ErrorCode.TIME_NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(ReservationNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of(ErrorCode.RESERVATION_NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(ThemeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleThemeNotFound(ThemeNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of(ErrorCode.THEME_NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(HolidayNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleHolidayNotFound(Exception e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(ErrorCode.HOLIDAY_NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnhandled(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."));
  }
}

