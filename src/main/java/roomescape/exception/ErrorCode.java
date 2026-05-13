package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다."),
    RESERVATION_ALREADY_CANCELED(HttpStatus.CONFLICT, "이미 취소됨"),
    RESERVATION_DUPLICATE(HttpStatus.CONFLICT, "동일한 날짜, 시간, 테마에 예약이 등록되어 있습니다."),
    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "해당 시간에 이미 예약이 있습니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마를 찾을 수 없습니다."),
    PAST_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "지난 시간은 예약할 수 없습니다.");

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
