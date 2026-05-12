package roomescape.time.presentation.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.advice.ApiExceptionHandlerSupport;
import roomescape.common.dto.ErrorResponse;
import roomescape.common.exception.RoomescapeException;
import roomescape.time.domain.exception.ReservationTimeInUseException;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

@RestControllerAdvice
public class ReservationTimeExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ReservationTimeExceptionHandler.class);

    @ExceptionHandler({
            ReservationTimeInUseException.class,
            ReservationTimeNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleReservationTimeException(
            RoomescapeException e, HttpServletRequest request) {
        return handleRoomescapeException(e, request, log);
    }
}
