package roomescape.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.dto.ErrorInformation;
import roomescape.common.validation.exception.RequestValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String DEFAULT_ERROR_MESSAGE = "해당 요청을 처리할 수 없습니다.";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorInformation> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInformation> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ErrorInformation> handleRequestValidationException(RequestValidationException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInformation> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, DEFAULT_ERROR_MESSAGE);
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorInformation> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInformation> handleUnknownException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

}
