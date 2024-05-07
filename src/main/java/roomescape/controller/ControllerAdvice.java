package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.controller.dto.ErrorMessageResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageResponse> handleRuntimeException() {
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse("서버 내부 에러가 발생하였습니다.");
        return ResponseEntity.internalServerError().body(errorMessageResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
