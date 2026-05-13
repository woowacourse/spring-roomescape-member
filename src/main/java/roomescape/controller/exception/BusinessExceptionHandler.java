package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseEntityException;

@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            EntityNotFoundException.class,
            InUseEntityException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        log.warn("[Bad Request]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(response);
    }
}
