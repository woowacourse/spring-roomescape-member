package roomescape.global;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ResponseErrorMessage;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseErrorMessage> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ResponseErrorMessage response = ResponseErrorMessage.of(errorCode, e.getMessage());

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorMessage> handleException(Exception e) {
        log.error("Unexpected error [Exception]", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseErrorMessage.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] ", e);

        String message = "요청 형식이 올바르지 않습니다.";

        if (e.getCause() instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().get(0).getFieldName();
            message = String.format("필드 '%s'의 형식이 잘못되었습니다.", fieldName);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseErrorMessage.of(ErrorCode.INVALID_REQUEST, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorMessage> handleValidation(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ", e);
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseErrorMessage.of(ErrorCode.INVALID_REQUEST, message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseErrorMessage> handleIllegalArgument(IllegalArgumentException e) {
        log.error("[IllegalArgumentException] ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseErrorMessage.of(ErrorCode.INVALID_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseErrorMessage> handleNoSuchElement(NoSuchElementException e) {
        log.error("[NoSuchElementException] ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseErrorMessage.of(ErrorCode.NOT_FOUND));
    }
}
