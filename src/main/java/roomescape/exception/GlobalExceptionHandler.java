package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<FailureResponse> handleIllegalArgument(IllegalArgumentException ex) {
        FailureResponse failureResponse = new FailureResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(failureResponse, HttpStatus.BAD_REQUEST);
    }
}
