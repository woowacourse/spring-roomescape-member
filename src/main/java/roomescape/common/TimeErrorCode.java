package roomescape.common;

import org.springframework.http.HttpStatus;

public enum TimeErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다."),
    DUPLICATE_START_AT(HttpStatus.CONFLICT, "이미 존재하는 시간입니다"),
    REFERENCED_BY_RESERVATION(HttpStatus.CONFLICT, "예약이 존재하여 시간을 삭제할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

    TimeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
