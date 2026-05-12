package roomescape.domain.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorCode;

public enum ReservationErrorCode implements ErrorCode {

    INVALID_RESERVATION(HttpStatus.BAD_REQUEST, "예약 정보가 올바르지 않습니다."),
    PAST_RESERVATION(HttpStatus.BAD_REQUEST, "지나간 날짜·시간에 대한 예약은 불가능합니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "이미 동일한 날짜, 시간, 테마의 예약이 존재합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다.");

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
