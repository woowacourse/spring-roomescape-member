package roomescape.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ForbiddenAccessException;
import roomescape.exception.ResponseErrorMessage;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseErrorMessage> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ResponseErrorMessage response = ResponseErrorMessage.of(errorCode);

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Unexpected error [Exception]", e);
        return ResponseEntity.badRequest().body("서버 내부 오류가 발생했습니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] ", e);

        String message = "요청 형식이 올바르지 않습니다.";

        if (e.getCause() instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().get(0).getFieldName();
            message = String.format("필드 '%s'의 형식이 잘못되었습니다.", fieldName);
        }

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidation(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ", e);
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(messages);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        log.error("[IllegalArgumentException] ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException e) {
        log.error("[NoSuchElementException] ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<String> handleIForbiddenAccessException(ForbiddenAccessException e) {
        log.error("[ForbiddenAccessException] ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
