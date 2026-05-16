package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400
    INVALID_RESERVATION_NAME(HttpStatus.BAD_REQUEST, "예약자명이 존재하지 않습니다."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "예약 날짜는 존재하지 않습니다."),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "예약 시간이 존재하지 않습니다."),
    INVALID_THEME_NAME(HttpStatus.BAD_REQUEST, "테마명은 존재하지 않습니다."),

    // 404
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),

    // 409
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 예약된 시간입니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 있는 시간은 삭제할 수 없습니다."),

    // 422
    PAST_RESERVATION_DATE(HttpStatus.UNPROCESSABLE_ENTITY, "현재보다 이전의 날짜는 예약할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
