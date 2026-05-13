package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 적절하지 않습니다."),
    INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),

    RESERVATION_DUPLICATE(HttpStatus.CONFLICT, "이미 예약된 일정입니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다."),
    RESERVATION_PAST_DATETIME(HttpStatus.BAD_REQUEST, "과거 날짜, 시간으로 예약할 수 없습니다."),

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public String errorCode() {
        return this.name();
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
