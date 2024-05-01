package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    //TODO : 예외 사항 분리 필요
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    private ResponseEntity<String> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
