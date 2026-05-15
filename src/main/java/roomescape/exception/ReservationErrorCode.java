package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ReservationErrorCode implements ErrorCode {
    RESERVATION_DUPLICATE(HttpStatus.CONFLICT, "같은 날짜, 같은 시간에 예약이 존재합니다."),
    RESERVATION_PAST_TIME(HttpStatus.UNPROCESSABLE_ENTITY, "과거 예약은 선택 불가능합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."),
    INVALID_NAME(HttpStatus.BAD_REQUEST, "이름은 공백일 수 없으며 20자 이내여야 합니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "예약 날짜 형식이 올바르지 않습니다.");

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
    public String getErrorMessage() {
        return message;
    }
}
