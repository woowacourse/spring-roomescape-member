package roomescape.controller;

import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.ForbiddenAccessException;
import roomescape.exception.ResponseErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErrorMessage handleException(Exception e) {
        log.error("Unexpected error [Exception]", e);
        return new ResponseErrorMessage("서버 내부 오류가 발생했습니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return new ResponseErrorMessage("요청 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage handleValidation(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ", e);
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        return new ResponseErrorMessage(messages);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage handleIllegalArgument(IllegalArgumentException e) {
        log.error("[IllegalArgumentException] ", e);
        return new ResponseErrorMessage(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseErrorMessage handleNoSuchElement(NoSuchElementException e) {
        log.error("[NoSuchElementException] ", e);
        return new ResponseErrorMessage(e.getMessage());
    }
    
    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseErrorMessage handleIForbiddenAccessException(ForbiddenAccessException e) {
        log.error("[ForbiddenAccessException] ", e);
        return new ResponseErrorMessage(e.getMessage());
    }
}
