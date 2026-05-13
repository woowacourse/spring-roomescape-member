package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_001", "존재하지 않는 예약입니다."),
    DUPLICATE_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "RESERVATION_002", "이미 예약된 시간대입니다."),
    PAST_DATE_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "RESERVATION_003", "과거 날짜로는 예약할 수 없습니다."),

    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "TIME_001", "존재하지 않는 시간입니다."),
    TIME_IN_USE(HttpStatus.UNPROCESSABLE_ENTITY, "TIME_002", "예약이 존재하는 시간은 삭제할 수 없습니다."),

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "THEME_001", "존재하지 않는 테마입니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}