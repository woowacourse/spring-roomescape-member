package roomescape.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.common.dto.ErrorResponse;
import roomescape.common.exception.RoomescapeException;

public abstract class ApiExceptionHandlerSupport {

    protected ResponseEntity<ErrorResponse> handleRoomescapeException(
            RoomescapeException e, HttpServletRequest request, Logger log) {
        log.warn(
                "Handled exception [{} {}] status={}, message={}",
                request.getMethod(),
                request.getRequestURI(),
                e.getHttpStatus().value(),
                e.getMessage(),
                e
        );
        return response(e.getHttpStatus(), e.getMessage());
    }

    protected ResponseEntity<ErrorResponse> badRequest(String message) {
        return response(HttpStatus.BAD_REQUEST, message);
    }

    protected ResponseEntity<ErrorResponse> response(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponse.of(httpStatus, message));
    }
}
