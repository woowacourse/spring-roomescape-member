package roomescape.domain.reservationtime.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorCode;

public enum TimeErrorCode implements ErrorCode {

    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "예약 시간 정보가 올바르지 않습니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    TimeErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
