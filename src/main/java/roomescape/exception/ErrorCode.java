package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "[ERROR] 해당 ID의 예약을 찾을 수 없습니다."),
    NOT_FOUND_RESERVATION_TIME(HttpStatus.NOT_FOUND, "[ERROR] 해당 ID의 예약 시간을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "[ERROR] 서버 내부에서 에러가 발생했습니다.");

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
