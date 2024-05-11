package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String PREFIX = "[ERROR] ";

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    private ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(PREFIX + e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(PREFIX + e.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    private ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.badRequest().body(PREFIX + e.getMessage());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    private ResponseEntity<String> handleNullPointerException(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(PREFIX + "쿼리를 확인해주세요: " + e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(PREFIX + "예상치 못한 예외가 발생했습니다. : " + e);
    }
}
