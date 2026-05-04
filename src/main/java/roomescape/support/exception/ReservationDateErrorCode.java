package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationDateErrorCode implements ErrorCode {
    RESERVATION_DATE_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 날짜 입니다."),
    RESERVATION_DATE_IN_USE(HttpStatus.CONFLICT, "이미 얘약이 존재하는 날짜는 삭제할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ReservationDateErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
