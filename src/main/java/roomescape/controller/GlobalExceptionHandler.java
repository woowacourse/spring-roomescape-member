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
import roomescape.exception.ResponseMessageDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessageDto handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        printErrorStatus(e);
        return new ResponseMessageDto("요청 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessageDto handleValidation(MethodArgumentNotValidException e) {
        printErrorStatus(e);
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        return new ResponseMessageDto(messages);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessageDto handleIllegalArgument(IllegalArgumentException e) {
        printErrorStatus(e);
        return new ResponseMessageDto(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessageDto handleNoSuchElement(NoSuchElementException e) {
        printErrorStatus(e);
        return new ResponseMessageDto(e.getMessage());
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseMessageDto handleIForbiddenAccessException(ForbiddenAccessException e) {
        printErrorStatus(e);
        return new ResponseMessageDto(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessageDto handleException(Exception e) {
        printErrorStatus(e);
        return new ResponseMessageDto("서버 내부 오류가 발생했습니다.");
    }

    private void printErrorStatus(Exception e) {
        log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage(), e);
    }
}
