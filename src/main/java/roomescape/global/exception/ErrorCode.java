package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400
    INVALID_NAME(HttpStatus.BAD_REQUEST, "INVALID_NAME", "이름이 비어있습니다. 이름을 입력해 주세요."),
    INVALID_NAME_LENGTH(HttpStatus.BAD_REQUEST, "INVALID_NAME", "이름의 길이가 유효하지 않습니다. 입력값을 확인해 주세요."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "INVALID_ID", "식별자 값이 올바르지 않습니다. 새로고침 후 다시 시도해 주세요."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "존재하지 않거나 올바르지 않은 날짜입니다. 날짜를 다시 선택해 주세요."),

    // 404
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "THEME_NOT_FOUND", "등록되지 않은 테마입니다. 테마 목록을 다시 확인해 주세요."),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "TIME_NOT_FOUND", "등록되지 않은 시간입니다. 예약 가능한 시간을 다시 확인해 주세요."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", "존재하지 않는 예약입니다. 예약 정보를 다시 확인해 주세요."),

    // 409
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "RESERVATION_DUPLICATED", "이미 예약이 존재합니다. 다른 시간을 선택해 주세요."),
    RESERVATION_TIME_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "RESERVATION_TIME_DELETE_NOT_ALLOWED", "예약이 존재하는 시간은 삭제할 수 없습니다. 해당 시간의 예약을 먼저 취소해 주세요."),
    RESERVATION_IN_PAST_NOT_ALLOWED(HttpStatus.CONFLICT, "RESERVATION_IN_PAST_NOT_ALLOWED", "과거 시간은 예약할 수 없습니다. 현재 시각 이후의 일정을 선택해 주세요."),
    CANCEL_IN_PAST_NOT_ALLOWED(HttpStatus.CONFLICT, "CANCEL_IN_PAST_NOT_ALLOWED", "과거 예약은 취소할 수 없습니다. 지난 이용 내역을 확인해 주세요."),
    RESERVATION_ALREADY_CANCELLED(HttpStatus.CONFLICT, "RESERVATION_ALREADY_CANCELLED", "이미 취소된 예약입니다. 취소 내역을 확인해 주세요.");

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
