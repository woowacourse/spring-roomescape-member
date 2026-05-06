package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.reservation.DuplicateReservationException;
import roomescape.reservation.ReservationException;
import roomescape.reservation.ReservationNotFoundException;
import roomescape.reservationtime.DuplicateReservationTimeException;
import roomescape.reservationtime.ReservationTimeException;
import roomescape.reservationtime.ReservationTimeNotFoundException;
import roomescape.reservationtime.ReservationTimeNotEmptyException;
import roomescape.theme.ThemeDuplicateException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("유효하지 않은 요청입니다");

        ErrorResponse response = new ErrorResponse(message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReservation(DuplicateReservationException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponse> handleReservationException(ReservationException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeNotFound(ReservationTimeNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DuplicateReservationTimeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReservationTime(DuplicateReservationTimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ThemeDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateTheme(ThemeDuplicateException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ReservationTimeNotEmptyException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeNotEmpty(ReservationTimeNotEmptyException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ReservationTimeException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeException(ReservationTimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(Exception e) {
        logger.error("서버 내부 오류 발생", e);
        ErrorResponse response = new ErrorResponse("서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
