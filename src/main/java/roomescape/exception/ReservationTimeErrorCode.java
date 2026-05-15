package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ReservationTimeErrorCode implements ErrorCode {

    RESERVATION_TIME_DUPLICATE(HttpStatus.CONFLICT, "같은 시간이 존재합니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다.");

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
}
