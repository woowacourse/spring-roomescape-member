package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ReservationErrorCode implements ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다"),
    ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "이미 예약된 일시입니다"),
    PAST_RESERVATION(HttpStatus.BAD_REQUEST, "과거 일시로 예약할 수 없습니다"),
    ;

    private final HttpStatus status;
    private final String message;

    ReservationErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
