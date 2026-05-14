package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "해당 예약 시간에 예약이 존재하여 삭제할 수 없습니다."),
    RESERVATION_ALREADY_PAST(HttpStatus.UNPROCESSABLE_ENTITY, "과거 예약은 취소 불가능합니다."),
    RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 예약만 삭제할 수 있습니다.");

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
        return this.message;
    }
}
