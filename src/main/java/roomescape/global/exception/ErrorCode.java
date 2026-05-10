package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESERVATION_NOT_FOUND"),
    THEME_NOT_FOUND(HttpStatus.BAD_REQUEST, "THEME_NOT_FOUND"),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESERVATION_TIME_NOT_FOUND"),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "DUPLICATE_RESERVATION"),
    DUPLICATE_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "DUPLICATE_RESERVATION_TIME"),
    INVALID_RESERVATION(HttpStatus.BAD_REQUEST, "INVALID_RESERVATION"),
    INVALID_RESERVATION_PAGING(HttpStatus.BAD_REQUEST, "INVALID_RESERVATION_PAGING"),
    INVALID_RANKING_CONDITION(HttpStatus.BAD_REQUEST, "INVALID_RANKING_CONDITION"),
    REFERENCED_DATA(HttpStatus.BAD_REQUEST, "REFERENCED_DATA"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");

    private final HttpStatus status;
    private final String code;

    ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
