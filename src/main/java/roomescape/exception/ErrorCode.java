package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400
    RESERVATION_NAME_INVALID(HttpStatus.BAD_REQUEST, "예약자명은 영문, 숫자, 한글만 사용할 수 있습니다."),

    // 404
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),

    // 409
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 예약된 시간입니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 있는 시간은 삭제할 수 없습니다."),
    RESERVATION_TIME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 예약 시간입니다."),

    // 422
    RESERVATION_DATE_PAST(HttpStatus.UNPROCESSABLE_ENTITY, "현재보다 이전의 날짜는 예약할 수 없습니다.");

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
