package roomescape.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.domain.exception.PastReservationException;
import roomescape.domain.exception.ReserverNameEmptyException;
import roomescape.presentation.dto.response.ErrorResponse;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(ReserverNameEmptyException.class)
    public ResponseEntity<ErrorResponse> handleReserverNameEmptyException(ReserverNameEmptyException e) {
        String message = e.getMessage();
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<ErrorResponse> handlePastReservationException(PastReservationException e) {
        String message = e.getMessage();
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
