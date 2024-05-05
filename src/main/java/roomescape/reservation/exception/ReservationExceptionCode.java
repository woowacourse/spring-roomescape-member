package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.model.ExceptionCode;

public enum ReservationExceptionCode implements ExceptionCode {

    RESERVATION_TIME_IS_PAST_EXCEPTION(HttpStatus.BAD_REQUEST, "지난 시간의 테마를 선택했습니다."),
    NAME_IS_NULL_OR_BLANK_EXCEPTION(HttpStatus.BAD_REQUEST, "null 혹은 빈칸으로 이루어진 이름으로 예약을 시도하였습니다."),
    RESERVATION_DATE_IS_PAST_EXCEPTION(HttpStatus.BAD_REQUEST, "지난 날짜의 예약을 시도하였습니다."),
    ILLEGAL_NAME_FORM_EXCEPTION(HttpStatus.BAD_REQUEST, "특수문자가 포함된 이름으로 예약을 시도하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ReservationExceptionCode(HttpStatus httpStatus, String message) {
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
