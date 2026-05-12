package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT),
    DUPLICATE_RESERVATION_TIME(HttpStatus.BAD_REQUEST),
    INVALID_RESERVATION(HttpStatus.BAD_REQUEST),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST),
    INVALID_THEME(HttpStatus.BAD_REQUEST),
    INVALID_RESERVATION_PAGING(HttpStatus.BAD_REQUEST),
    INVALID_RANKING_CONDITION(HttpStatus.BAD_REQUEST),
    REFERENCED_DATA(HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
