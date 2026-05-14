package roomescape.domain.reservation.error.type;

import org.springframework.http.HttpStatus;
import roomescape.global.error.exception.type.ErrorType;

public enum ReservationErrorType implements ErrorType {
    ALREADY_RESERVED(HttpStatus.CONFLICT, "이미 예약된 날짜, 시간, 테마입니다."),
    FIELD_RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "조회할 자원이 존재하지 않습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ReservationErrorType(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus status() {
        return httpStatus;
    }

    @Override
    public String message() {
        return message;
    }
}
