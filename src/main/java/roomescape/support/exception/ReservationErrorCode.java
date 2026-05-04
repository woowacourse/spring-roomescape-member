package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorCode implements ErrorCode {
    INVALID_RESERVATION_NAME(HttpStatus.BAD_REQUEST, "이름은 비어 있을 수 없습니다."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "날짜는 필수입니다."),
    ;

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
