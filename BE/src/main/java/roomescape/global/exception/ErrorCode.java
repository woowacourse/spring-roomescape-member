package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_REQUEST_NULL(HttpStatus.BAD_REQUEST, "예약 데이터가 비어있습니다."),
    RESERVATION_NAME_EMPTY(HttpStatus.BAD_REQUEST, "예약자의 이름이 비어있습니다."),
    RESERVATION_DATE_NULL(HttpStatus.BAD_REQUEST, "예약 날짜가 비어있습니다."),
    RESERVATION_ID_NULL(HttpStatus.BAD_REQUEST, "예약 목록 ID가 비어있습니다."),

    // ReservationTime
    RESERVATION_TIME_ID_NULL(HttpStatus.BAD_REQUEST, "예약 시간이 비어있습니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다."),
    RESERVATION_TIME_REQUEST_NULL(HttpStatus.BAD_REQUEST, "예약 시간 데이터가 비어있습니다."),
    RESERVATION_TIME_START_AT_NULL(HttpStatus.BAD_REQUEST, "예약 시간이 비어있습니다."),
    RESERVATION_TIME_ALREADY_USED(HttpStatus.BAD_REQUEST, "참조하고 있는 예약 시간이어서 삭제할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
