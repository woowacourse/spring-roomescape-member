package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
    NO_COOKIE_EXIST(HttpStatus.BAD_REQUEST, "쿠키가 존재하지 않습니다."),
    NO_TOKEN_EXIST(HttpStatus.BAD_REQUEST, "쿠키에 토큰이 존재하지 않습니다."),
    NO_SUCH_CLAIM(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 claim 이름입니다."),

    RESERVATION_NOT_ALLOWED_IN_PAST(HttpStatus.CONFLICT, "지난 날짜나 시간은 예약이 불가능합니다."),
    RESERVATION_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 해당 날짜/시간/테마에 예약이 존재합니다."),
    TIME_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 예약 시간입니다."),
    RESERVATION_EXIST_ON_TIME(HttpStatus.CONFLICT, "해당 시간에 예약이 존재하여 시간을 삭제할 수 없습니다.");


    private final HttpStatus status;
    private final String message;

    ExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
