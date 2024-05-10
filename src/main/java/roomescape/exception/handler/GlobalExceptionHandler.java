package roomescape.exception.handler;

import java.util.Objects;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import roomescape.exception.AuthorizationException;
import roomescape.exception.RoomEscapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RoomEscapeException.class)
    public ErrorResponse handleRoomEscapeException(final RoomEscapeException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ErrorResponse handleAuthorizationException(final AuthorizationException ex) {
        return ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, Objects.requireNonNull(ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage())).build();
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        return ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).build();
    }
}
