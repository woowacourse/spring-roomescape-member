package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    DATA_CONFLICT(HttpStatus.CONFLICT, "요청한 데이터가 현재 상태와 충돌합니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다."),
    THEME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 존재하는 테마는 삭제할 수 없습니다."),

    INVALID_RESERVATION(HttpStatus.BAD_REQUEST, "예약 정보가 올바르지 않습니다."),
    PAST_RESERVATION(HttpStatus.BAD_REQUEST, "지나간 날짜·시간에 대한 예약은 불가능합니다."),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "이미 동일한 날짜, 시간, 테마의 예약이 존재합니다."),

    INVALID_THEME(HttpStatus.BAD_REQUEST, "테마 정보가 올바르지 않습니다."),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "예약 시간 정보가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
