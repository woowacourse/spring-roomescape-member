package roomescape.presentation.exception;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.application.exception.AuthException;
import roomescape.domain.exception.PastReservationException;
import roomescape.presentation.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "알 수 없는 문제가 발생했습니다.";

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<ErrorResponse> handlePastReservationException(PastReservationException e) {
        String message = e.getMessage();
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        String message = e.getMessage();
        log.info(message);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, message);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();
        log.warn(message);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.CONFLICT, message);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        String message = e.getMessage();
        log.warn(message);
        ErrorResponse errorResponse = ErrorResponse.of(e.getStatus(), message);
        return ResponseEntity
                .status(e.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn(message);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
