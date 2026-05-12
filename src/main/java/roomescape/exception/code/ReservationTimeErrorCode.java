package roomescape.exception.code;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum ReservationTimeErrorCode implements ErrorCode {

    RESERVATION_TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 예약 시간입니다."),
    RESERVATION_TIME_HAS_RESERVATION(HttpStatus.BAD_REQUEST, "해당 예약 시간에 예약이 존재합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ReservationTimeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
