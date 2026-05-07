package roomescape.exception.code;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_ALREADY_EXISTS("R-409-001", HttpStatus.CONFLICT, "해당 시간에 예약이 존재합니다."),
    RESERVATION_NOT_FOUND("R-404-002", HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ReservationErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
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
