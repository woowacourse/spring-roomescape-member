package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseTimeException;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            InUseTimeException.class,
            EntityNotFoundException.class,
            InvalidReservationException.class,
            InvalidReservationTimeException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        log.warn("[Bad Request] ", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception) {
        log.error("[Internal Server Error] ", exception);
        ErrorResponse response = new ErrorResponse("예상하지 못한 예외가 발생했습니다.");

        return ResponseEntity.internalServerError()
                .body(response);
    }
}
