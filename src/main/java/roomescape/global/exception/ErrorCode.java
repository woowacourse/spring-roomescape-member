package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "이름은 필수입니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식이 잘못되었습니다. (yyyy-MM-dd)"),
    CANNOT_MODIFY_OTHER_RESERVATION(HttpStatus.FORBIDDEN, "다른 사람 예약을 변경할 수 없습니다."),
    CANNOT_DELETE_OTHER_RESERVATION(HttpStatus.FORBIDDEN, "다른 사람 예약을 취소할 수 없습니다."),
    PAST_RESERVATION(HttpStatus.BAD_REQUEST, "지난 시간에는 예약할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 예약이 존재합니다."),
    CANNOT_DELETE_RESERVED_TIME(HttpStatus.CONFLICT, "이미 예약이 존재하는 시간대이므로 삭제할 수 없습니다."),
    ;

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
