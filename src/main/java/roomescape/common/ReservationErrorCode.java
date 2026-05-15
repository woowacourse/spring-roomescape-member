package roomescape.common;

import org.springframework.http.HttpStatus;

public enum ReservationErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    DUPLICATE(HttpStatus.CONFLICT, "이미 예약된 시간입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ReservationErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
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
