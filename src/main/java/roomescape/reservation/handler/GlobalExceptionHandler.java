package roomescape.reservation.handler;

import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.reservation.handler.exception.CustomException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger;

    public GlobalExceptionHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleInvalidPostTime(DateTimeParseException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>("""
                날짜/시간 입력 형식이 잘못되었습니다.
                """, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleInvalidPostTime(CustomException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleInvalidNumber(NumberFormatException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>("""
                잘못된 숫자 형식입니다.
                """, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidRequestForm(NumberFormatException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>("""
                요청 형식이 잘못되었습니다.
                """, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>("""
                예기치 못한 런타임 오류가 발생했습니다.
                """, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>("""
                예기치 못한 오류가 발생했습니다.
                """, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
