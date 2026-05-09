package roomescape.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ReservationNotFoundException.class,
            ReservationTimeNotFoundException.class
    })
    public ResponseEntity<String> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ReservationDuplicatedException.class)
    public ResponseEntity<String> handleReservationAlreadyExists(ReservationDuplicatedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("작업을 처리할 수 없습니다. : %s".formatted(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("예상하지 못한 오류가 발생했습니다. : %s".formatted(e.getMessage()));
    }
}
