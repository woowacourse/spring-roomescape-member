package roomescape.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간이 존재하지 않습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."),
    HOLIDAY_NOT_FOUND(HttpStatus.NOT_FOUND, "휴일이 존재하지 않습니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마가 존재하지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다."),
    PAST_RESERVATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "지난 날짜와 시간으로 예약할 수 없습니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "이미 예약된 시간입니다."),
    RESERVED_TIME_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
