package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String NULL_POINTER_ERROR_MESSAGE = "인자 중 null 값이 존재합니다.";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(NULL_POINTER_ERROR_MESSAGE));
    }
}
