package roomescape.time.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.time.domain.exception.ReservationTimeNullException;

@RestControllerAdvice
public class ReservationTimeExceptionHandler {

    @ExceptionHandler(ReservationTimeNullException.class)
    public ResponseEntity<String> handelReservationTimeNullException(ReservationTimeNullException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
