package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "해당 예약 시간에 예약이 존재하여 삭제할 수 없습니다."),
    RESERVATION_TIME_CONFLICT(HttpStatus.CONFLICT, "해당 시간이 이미 존재합니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다."),

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),
    THEME_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 테마 이름입니다."),
    THEME_IN_USE(HttpStatus.CONFLICT, "해당 테마에 예약이 존재하여 삭제할 수 없습니다."),

    RESERVATION_ALREADY_PAST(HttpStatus.UNPROCESSABLE_ENTITY, "과거 예약은 취소 및 변경 불가 합니다."),
    RESERVATION_CREATE_TO_PAST(HttpStatus.UNPROCESSABLE_ENTITY, "과거 날짜와 시간으로는 예약할 수 없습니다."),
    RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 예약만 접근할 수 있습니다."),
    RESERVATION_CONFLICT(HttpStatus.CONFLICT, "해당 시간에 이미 예약이 존재합니다."),
    RESERVATION_UPDATE_TO_PAST(HttpStatus.UNPROCESSABLE_ENTITY, "과거 시간으로 변경할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    INVALID_RESERVATION_NAME(HttpStatus.BAD_REQUEST, "이름 형식이 잘못되었습니다."),
    INVALID_THEME_NAME(HttpStatus.BAD_REQUEST, "테마 이름 형식이 잘못되었습니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 잘못되었습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return this.message;
    }
}
