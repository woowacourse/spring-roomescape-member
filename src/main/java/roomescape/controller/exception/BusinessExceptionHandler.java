package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseEntityException;
import roomescape.exception.NotAcceptableReservationException;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            InUseEntityException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        log.warn("[Bad Request]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(NotAcceptableReservationException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(NotAcceptableReservationException exception) {
        log.warn("[Forbidden]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException exception) {
        log.warn("[Not Found]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DuplicateReservationException exception) {
        log.warn("[Conflict]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }
}
