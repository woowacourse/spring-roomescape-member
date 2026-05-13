package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    RESERVATION_TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 예약 시간입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "예약이 존재하는 시간입니다.");

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
        return message;
    }
}
