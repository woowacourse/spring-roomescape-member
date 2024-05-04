package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import roomescape.domain.exception.IllegalNullArgumentException;
import roomescape.dto.ErrorResponse;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, IllegalNullArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        return ResponseEntity
                .badRequest()
                // TODO: Response 메시지 변경
                .body(new ErrorResponse(e.getMessage()));
    }
}
