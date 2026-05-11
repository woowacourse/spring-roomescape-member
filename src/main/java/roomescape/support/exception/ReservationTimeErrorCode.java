package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationTimeErrorCode implements ErrorCode {

    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "시간은 필수입니다."),
    INVALID_RESERVATION_TIME_FORMAT(HttpStatus.BAD_REQUEST, "시간은 HH:MM 형식이어야 합니다."),
    RESERVATION_TIME_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간대 입니다."),
    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "이미 예약이 존재하는 시간대는 삭제할 수 없습니다."),
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
}
