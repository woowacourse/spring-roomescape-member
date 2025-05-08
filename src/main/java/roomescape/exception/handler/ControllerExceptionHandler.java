package roomescape.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.dto.ErrorResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.PastReservationTimeException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("시간 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(final DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleDeleteNotAllowedException(final DeletionNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReservationException(final DuplicateReservationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(PastReservationTimeException.class)
    public ResponseEntity<ErrorResponse> handlePastReservationTimeException(final PastReservationTimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex));
    }
}
