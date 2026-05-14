package roomescape.common;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.exception.ReservationPastDateTimeException;
import roomescape.reservationtime.exception.ReservationTimeDuplicatedException;
import roomescape.reservationtime.exception.ReservationTimeInUseException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.theme.exception.ThemeInUseException;
import roomescape.theme.exception.ThemeNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ReservationNotFoundException.class,
            ReservationTimeNotFoundException.class,
            ThemeNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({
            ReservationDuplicatedException.class,
            ReservationTimeDuplicatedException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicatedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({
            ReservationTimeInUseException.class,
            ThemeInUseException.class
    })
    public ResponseEntity<ErrorResponse> handleEntityInUseException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("요청 본문 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(ReservationPastDateTimeException.class)
    public ResponseEntity<ErrorResponse> handleReservationPastDateTimeException(ReservationPastDateTimeException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(e.getMessage()));
    }

}
