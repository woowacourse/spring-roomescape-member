package roomescape.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.CustomException;

@RestControllerAdvice(basePackages = "roomescape.controller.api")
public class RestExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatusCode())
                        .body(new CustomExceptionResponse(e.getDetails()));
    }
}
