package roomescape.reservation.presentation.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.advice.ApiExceptionHandlerSupport;
import roomescape.common.dto.ErrorResponse;
import roomescape.reservation.domain.exception.DuplicateReservationException;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
import roomescape.reservation.domain.exception.ReservationOwnerMismatchException;

@RestControllerAdvice(basePackages = "roomescape.reservation")
public class ReservationExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ReservationExceptionHandler.class);

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(
            ReservationNotFoundException e, HttpServletRequest request) {
        logHandled(HttpStatus.NOT_FOUND, e, request, log);
        return response(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ReservationOwnerMismatchException.class)
    public ResponseEntity<ErrorResponse> handleReservationOwnerMismatch(
            ReservationOwnerMismatchException e, HttpServletRequest request) {
        logHandled(HttpStatus.NOT_FOUND, e, request, log);
        return response(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReservation(
            DuplicateReservationException e, HttpServletRequest request) {
        logHandled(HttpStatus.CONFLICT, e, request, log);
        return response(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<ErrorResponse> handlePastReservation(
            PastReservationException e, HttpServletRequest request) {
        logHandled(HttpStatus.CONFLICT, e, request, log);
        return response(HttpStatus.CONFLICT, e.getMessage());
    }
}
