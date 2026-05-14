package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.CodeException;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.exception.InUseEntityException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotAcceptableReservationException;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErrorResponse> handleDefaultBadRequest(Exception exception) {
        log.warn("[Bad Request]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage(), ErrorCode.BAD_REQUEST);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(InvalidInputException exception) {
        log.warn("[Bad Request]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrorCode());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(NotAcceptableReservationException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(NotAcceptableReservationException exception) {
        log.warn("[Forbidden]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrorCode());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException exception) {
        log.warn("[Not Found]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrorCode());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler({
            InUseEntityException.class,
            DuplicateReservationException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(CodeException exception) {
        log.warn("[Conflict]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrorCode());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }
}
