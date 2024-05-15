package roomescape.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import roomescape.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_PREFIX = "exception occur: {}";
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<String> handleValidationException(BindException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logger.warn(ERROR_PREFIX, errorMessage);
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<String> handleJsonParsingException(Exception exception) {
        logger.warn(ERROR_PREFIX, exception.getMessage());
        return new ResponseEntity<>("유효하지 않은 필드가 존재합니다.", BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        logger.warn(ERROR_PREFIX, exception.getMessage());
        return new ResponseEntity<>("서버 에러입니다.", INTERNAL_SERVER_ERROR);
    }
}
