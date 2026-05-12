package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),

    // reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."),
    RESERVATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "자신의 예약만 접근할 수 있습니다."),
    RESERVATION_ALREADY_CANCELLED(HttpStatus.CONFLICT, "이미 예약이 취소되었습니다."),
    RESERVATION_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 예약이 완료되었습니다."),
    RESERVATION_TIME_OUT(HttpStatus.UNPROCESSABLE_ENTITY, "이미 예약 시간이 지났습니다."),
    RESERVATION_NOT_ALLOWED_DATE(HttpStatus.UNPROCESSABLE_ENTITY, "과거 날짜 시간은 예약할 수 없습니다."),
    RESERVATION_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 해당 날짜 시간 테마에 예약이 존재합니다."),

    // time
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "시간이 존재하지 않습니다."),
    TIME_HAS_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "예약에 해당하는 시간이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
