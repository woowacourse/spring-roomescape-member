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

    // TODO: static 인데 대문자를 안 쓰나?
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // TODO: 메서드 순서에 따라 에러 처리 결과가 달라지는지?
    // TODO: 처리 순서가 어떻게 되는지? (하위 구현체 먼저? 메서드 선언 순서?)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessageDto handleException(Exception e) {
        // TODO: log 에서 제공해주는 문자열 형식 알아보기
        log.error("Unexpected error [Exception]", e);
        return new ResponseMessageDto("서버 내부 오류가 발생했습니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessageDto handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return new ResponseMessageDto("요청 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessageDto handleValidation(MethodArgumentNotValidException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        return new ResponseMessageDto(messages);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessageDto handleIllegalArgument(IllegalArgumentException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return new ResponseMessageDto(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessageDto handleNoSuchElement(NoSuchElementException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return new ResponseMessageDto(e.getMessage());
    }
    
    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseMessageDto handleIForbiddenAccessException(ForbiddenAccessException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return new ResponseMessageDto(e.getMessage());
    }
}
