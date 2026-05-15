package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ReservationTimeErrorCode implements ErrorCode {

    RESERVATION_TIME_DUPLICATE(HttpStatus.CONFLICT, "같은 시간이 존재합니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다."),
    RESERVATION_TIME_INVALID_ID(HttpStatus.BAD_REQUEST, "ID의 형식이 올바르지 않습니다."),
    RESERVATION_EXIST_ON_TIME(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다."),
    INVALID_TIME(HttpStatus.BAD_REQUEST, "예약 시간 형식이 올바르지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    ReservationTimeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public String getErrorName() {
        return this.name();
    }
}
