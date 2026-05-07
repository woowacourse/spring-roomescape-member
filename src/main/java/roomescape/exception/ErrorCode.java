package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다."),
    RESERVATION_TIME_IN_USE(HttpStatus.BAD_REQUEST, "예약 시간에 해당하는 예약이 있습니다.");

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
