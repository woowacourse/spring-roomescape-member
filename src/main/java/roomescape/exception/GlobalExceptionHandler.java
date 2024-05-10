package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleArgumentException(final CustomException exception) {
        return new ResponseEntity<>(ErrorResponse.from(exception), exception.getStatus());
    }

    @ExceptionHandler(value = CustomException2.class)
    public ResponseEntity<ErrorResponse> handleArgumentException2(final CustomException2 exception) {
        return new ResponseEntity<>(ErrorResponse.from(exception), HttpStatus.BAD_REQUEST);
    }
}
