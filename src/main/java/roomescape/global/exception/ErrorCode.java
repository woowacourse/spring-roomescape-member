package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400
    INVALID_NAME(HttpStatus.BAD_REQUEST, "INVALID_NAME", "이름이 비어있습니다."),
    INVALID_NAME_LENGTH(HttpStatus.BAD_REQUEST, "INVALID_NAME", "이름의 길이가 유효하지 않습니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "INVALID_ID", "식별자 값이 올바르지 않습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "날짜 형식이 올바르지 않습니다."),
    INVALID_DATE_VALUE(HttpStatus.BAD_REQUEST, "INVALID_DATE_VALUE", "존재하지 않는 날짜입니다."),
    THEME_NOT_FOUND(HttpStatus.BAD_REQUEST, "THEME_NOT_FOUND", "등록되지 않은 테마입니다."),
    TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "TIME_NOT_FOUND", "등록되지 않은 시간입니다."),
    INVALID_REQUEST_TYPE(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_TYPE", "요청 값 타입이 올바르지 않습니다."),

    //404
    RESERVATION_ALREADY_CANCELED(HttpStatus.NOT_FOUND, "RESERVATION_ALREADY_CANCELED", "이미 취소된 예약입니다."),

    // 409
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "RESERVATION_DUPLICATED", "이미 예약이 존재합니다."),
    RESERVATION_TIME_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "RESERVATION_TIME_DELETE_NOT_ALLOWED", "예약이 존재하는 시간은 삭제할 수 없습니다."),
    RESERVATION_IN_PAST_NOT_ALLOWED(HttpStatus.CONFLICT, "RESERVATION_IN_PAST_NOT_ALLOWED", "과거 시간에는 예약할 수 없습니다."),
    CANCEL_IN_PAST_NOT_ALLOWED(HttpStatus.CONFLICT, "CANCEL_IN_PAST_NOT_ALLOWED", "과거 예약은 취소할 수 없습니다.");

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
