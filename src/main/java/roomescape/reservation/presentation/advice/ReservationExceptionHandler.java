package roomescape.reservation.presentation.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.advice.ApiExceptionHandlerSupport;
import roomescape.common.dto.ErrorResponse;
import roomescape.common.exception.RoomescapeException;
import roomescape.reservation.domain.exception.DuplicateReservationException;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;

@RestControllerAdvice
public class ReservationExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ReservationExceptionHandler.class);

    @ExceptionHandler({
            ReservationNotFoundException.class,
            PastReservationException.class,
            DuplicateReservationException.class
    })
    public ResponseEntity<ErrorResponse> handleReservationException(
            RoomescapeException e, HttpServletRequest request) {
        return handleRoomescapeException(e, request, log);
    }
}
