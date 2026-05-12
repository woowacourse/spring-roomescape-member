package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.ErrorPolicy;

public enum ReservationErrorPolicy implements ErrorPolicy {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "예약이 이미 존재합니다."),
    INVALID_RESERVATION_DATE(HttpStatus.UNPROCESSABLE_ENTITY,  "예약 날짜가 유효하지 않습니다."),
    INVALID_RESERVATION_REQUEST(HttpStatus.BAD_REQUEST, "예약 요청 값이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ReservationErrorPolicy(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}
