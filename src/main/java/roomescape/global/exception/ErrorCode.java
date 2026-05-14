package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400
    INVALID_NAME(HttpStatus.BAD_REQUEST, "INVALID_NAME", "이름이 비어있습니다."),
    INVALID_NAME_LENGTH(HttpStatus.BAD_REQUEST, "INVALID_NAME", "이름의 길이가 유효하지 않습니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "INVALID_ID", "식별자 값이 올바르지 않습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "날짜 형식이 올바르지 않거나 존재하지 않는 날짜입니다. 날짜 입력값을 다시 확인해주세요."),
    
    // 404
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "THEME_NOT_FOUND", "등록되지 않은 테마입니다."),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "TIME_NOT_FOUND", "등록되지 않은 시간입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", "존재하지 않는 예약입니다."),

    // 409
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "RESERVATION_DUPLICATED", "이미 예약이 존재합니다."),
    RESERVATION_TIME_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "RESERVATION_TIME_DELETE_NOT_ALLOWED", "예약이 존재하는 시간은 삭제할 수 없습니다."),
    RESERVATION_IN_PAST_NOT_ALLOWED(HttpStatus.CONFLICT, "RESERVATION_IN_PAST_NOT_ALLOWED", "과거 시간은 예약할 수 없습니다. 다른 날짜 및 시간을 선택해주세요."),
    CANCEL_IN_PAST_NOT_ALLOWED(HttpStatus.CONFLICT, "CANCEL_IN_PAST_NOT_ALLOWED", "과거 예약은 취소할 수 없습니다."),
    RESERVATION_ALREADY_CANCELLED(HttpStatus.CONFLICT, "RESERVATION_ALREADY_CANCELLED", "이미 취소된 예약입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
