package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionApiController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionInfo> IllegalArgumentExceptionHandler(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ExceptionInfo(exception.getMessage()));
    }
}
