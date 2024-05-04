package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import roomescape.dto.ErrorResponse;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
