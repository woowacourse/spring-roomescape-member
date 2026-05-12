package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.ReservationByPastDateTimeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationByPastDateTimeException.class)
    public ResponseEntity<String> handleIllegalArgumentException(ReservationByPastDateTimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
