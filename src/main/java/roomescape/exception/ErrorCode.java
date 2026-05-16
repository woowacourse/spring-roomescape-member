package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST),
    PAST_RESERVATION(HttpStatus.BAD_REQUEST),
    PAST_RESERVATION_LOCKED(HttpStatus.CONFLICT),
    FORBIDDEN_RESERVATION(HttpStatus.FORBIDDEN),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT),
    UNCHANGED_RESERVATION(HttpStatus.CONFLICT),
    RESOURCE_IN_USE(HttpStatus.CONFLICT),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
