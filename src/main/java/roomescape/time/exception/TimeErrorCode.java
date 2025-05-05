package roomescape.time.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum TimeErrorCode implements ErrorCode {
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "시간 정보를 찾을 수 없습니다"),
    TIME_DELETE_CONFLICT(HttpStatus.BAD_REQUEST, "예약이 존재하는 시간은 삭제할 수 없습니다"),
    ;

    private final HttpStatus status;
    private final String message;

    TimeErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
