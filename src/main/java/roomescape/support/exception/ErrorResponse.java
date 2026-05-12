package roomescape.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ErrorResponse(
    String code,
    String message
) {

    public static ResponseEntity<ErrorResponse> of(HttpStatus httpStatus, RoomescapeException exception) {
        return ResponseEntity.status(httpStatus)
            .body(new ErrorResponse(exception.getErrorCode().getCode(), exception.getMessage()));
    }

    public static ResponseEntity<ErrorResponse> of(HttpStatus httpStatus, ErrorCode errorCode) {
        return ResponseEntity.status(httpStatus)
            .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }
}
