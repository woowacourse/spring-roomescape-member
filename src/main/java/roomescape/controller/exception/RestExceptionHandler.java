package roomescape.controller.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.service.AuthorizationException;

@ControllerAdvice("roomescape.controller.api")
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(e.getMessage());

        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(e.getMessage());

        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthorizationException(AuthorizationException e) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}
