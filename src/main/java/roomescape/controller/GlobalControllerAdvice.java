package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.controller.dto.ErrorResponse;
import roomescape.exception.InvalidAuthorizationException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(exception = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(exception = InvalidAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(InvalidAuthorizationException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorResponse> responses = createValidationErrorMessage(e.getBindingResult());

        return ResponseEntity.badRequest().body(responses);
    }

    private List<ErrorResponse> createValidationErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(ErrorResponse::from)
                .toList();
    }
}
