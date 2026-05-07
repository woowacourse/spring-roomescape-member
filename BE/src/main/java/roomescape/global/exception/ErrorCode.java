package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_REQUEST_NULL(HttpStatus.BAD_REQUEST, "예약 데이터가 비어있습니다."),
    RESERVATION_NAME_EMPTY(HttpStatus.BAD_REQUEST, "예약자의 이름이 비어있습니다."),
    RESERVATION_DATE_NULL(HttpStatus.BAD_REQUEST, "예약 날짜가 비어있습니다."),
    RESERVATION_ID_NULL(HttpStatus.BAD_REQUEST, "예약 목록 ID가 비어있습니다."),
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "동일한 날짜, 시간, 테마의 예약이 이미 존재합니다."),

    // ReservationTime
    RESERVATION_TIME_ID_NULL(HttpStatus.BAD_REQUEST, "예약 시간이 비어있습니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다."),
    RESERVATION_TIME_REQUEST_NULL(HttpStatus.BAD_REQUEST, "예약 시간 데이터가 비어있습니다."),
    RESERVATION_TIME_START_AT_NULL(HttpStatus.BAD_REQUEST, "예약 시간이 비어있습니다."),
    RESERVATION_TIME_ALREADY_USED(HttpStatus.BAD_REQUEST, "참조하고 있는 예약 시간이어서 삭제할 수 없습니다."),

    // Theme
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마를 찾을 수 없습니다."),
    THEME_ID_NULL(HttpStatus.BAD_REQUEST, "테마 ID가 비어있습니다."),
    THEME_REQUEST_DESCRIPTION_NULL(HttpStatus.BAD_REQUEST, "테마 설명은 null 혹은 빈 값일 수 없습니다"),
    THEME_REQUEST_NAME_NULL(HttpStatus.BAD_REQUEST, "테마 이름은 null 혹은 빈 값일 수 없습니다"),
    THEME_REQUEST_THUMBNAIL_NULL(HttpStatus.BAD_REQUEST, "테마 썸네일은 null 혹은 빈 값일 수 없습니다"),
    THEME_ALREADY_USED(HttpStatus.BAD_REQUEST, "참조하고 있는 테마이어서 삭제할 수 없습니다.");

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
