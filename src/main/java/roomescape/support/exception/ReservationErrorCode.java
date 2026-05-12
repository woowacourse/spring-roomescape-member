package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorCode implements ErrorCode {
    INVALID_RESERVATION_NAME(HttpStatus.BAD_REQUEST, "이름은 비어 있을 수 없습니다."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "날짜는 필수입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약건 입니다"),
    RESERVATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "당일 및 지난 예약은 취소할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

    ReservationErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
