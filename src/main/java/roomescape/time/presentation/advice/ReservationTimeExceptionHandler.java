package roomescape.time.presentation.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.advice.ApiExceptionHandlerSupport;
import roomescape.common.dto.ErrorResponse;
import roomescape.time.domain.exception.ReservationTimeInUseException;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

@RestControllerAdvice
public class ReservationTimeExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ReservationTimeExceptionHandler.class);

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeNotFound(
            ReservationTimeNotFoundException e, HttpServletRequest request) {
        logHandled(HttpStatus.NOT_FOUND, e, request, log);
        return response(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ReservationTimeInUseException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeInUse(
            ReservationTimeInUseException e, HttpServletRequest request) {
        logHandled(HttpStatus.CONFLICT, e, request, log);
        return response(HttpStatus.CONFLICT, e.getMessage());
    }
}
