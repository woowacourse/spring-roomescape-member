package roomescape.reservation.controller;

import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.dto.ErrorResponse;

@Order(100)
@Slf4j
@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.status(ErrorCode.INVALID_DATE.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_DATE, e.getMessage()));
    }
}
