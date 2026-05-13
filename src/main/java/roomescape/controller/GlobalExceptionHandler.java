package roomescape.controller;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ErrorResponse;
import roomescape.exception.RoomEscapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        printErrorStatus(e);
        return parseOf(ErrorCode.INVALID_REQUEST_FORMAT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException e
    ) {
        printErrorStatus(e);
        return parseFrom(ErrorCode.INVALID_REQUEST_URI_VARIABLE_TYPE, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        printErrorStatus(e);
        String joinedMessage = e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));

        return parseFrom(ErrorCode.VALIDATION_FAILED, joinedMessage);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        printErrorStatus(e);
        return parseOf(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException e) {
        printErrorStatus(e);
        return parseOf(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(RoomEscapeException e) {
        printErrorStatus(e);
        return parseOf(e.getCode());
    }

    private void printErrorStatus(Exception e) {
        log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage(), e);
    }

    private ResponseEntity<ErrorResponse> parseOf(ErrorCode errorCode) {
        return ResponseEntity
            .status(errorCode.getCode())
            .body(ErrorResponse.of(errorCode));
    }

    private ResponseEntity<ErrorResponse> parseFrom(ErrorCode errorCode, String message) {
        return ResponseEntity
            .status(errorCode.getCode())
            .body(ErrorResponse.from(errorCode, message));
    }
}
