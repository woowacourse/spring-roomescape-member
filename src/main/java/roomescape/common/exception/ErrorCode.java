package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    DATA_CONFLICT(HttpStatus.CONFLICT, "요청한 데이터가 현재 상태와 충돌합니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다."),
    PAST_DATE_RESERVATION(HttpStatus.BAD_REQUEST, "과거 날짜로 예약할 수 없습니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "이미 예약된 시간입니다."),
    RESERVATION_OWNER_MISMATCH(HttpStatus.FORBIDDEN, "본인의 예약만 취소할 수 있습니다.");

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
