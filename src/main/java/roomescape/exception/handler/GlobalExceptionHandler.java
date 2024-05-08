package roomescape.exception.handler;

import java.util.Objects;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import roomescape.exception.RoomEscapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RoomEscapeException.class)
    public ProblemDetail handleRoomEscapeException(final RoomEscapeException ex) {
        return ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, Objects.requireNonNull(ex.getBindingResult()
                                                                                                 .getFieldErrors()
                                                                                                 .get(0)
                                                                                                 .getDefaultMessage()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
