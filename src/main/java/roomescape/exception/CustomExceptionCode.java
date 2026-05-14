package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum CustomExceptionCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다.");

    private final HttpStatus status;
    private final String message;

    CustomExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
