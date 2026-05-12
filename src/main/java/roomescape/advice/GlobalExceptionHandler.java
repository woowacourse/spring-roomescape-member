package roomescape.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.BusinessException;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.ReservationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of("INTERNAL_SERVER_ERROR", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(HttpMessageNotReadableException e) {
        BusinessException businessException =
                resolveHttpMessageNotReadableException(e);

        if (businessException == null) {
            return ResponseEntity
                    .badRequest()
                    .body(ErrorResponse.of(
                                    "MALFORMED_JSON_REQUEST",
                                    "요청 본문 형식이 올바르지 않습니다."
                            )
                    );
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(businessException));
    }

    public BusinessException resolveHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ) {
        Throwable cause = e;

        while (cause != null) {
            if (cause instanceof BusinessException be) {
                return be;
            }

            cause = cause.getCause();
        }

        return null;
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReservation(DuplicateReservationException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(e));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(e));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("INVALID_ARGUMENT", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("INVALID_STATE", e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("DATA_INTEGRITY_VIOLATION", e.getMessage()));
    }
}
