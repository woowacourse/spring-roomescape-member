package roomescape.domain.reservationtime.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorCode;

public enum TimeErrorCode implements ErrorCode {

    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    TimeErrorCode(HttpStatus status, String message) {
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
