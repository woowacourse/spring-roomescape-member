package roomescape.time.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.ErrorPolicy;

public enum TimeErrorPolicy implements ErrorPolicy {
    INVALID_TIME_START_AT(HttpStatus.UNPROCESSABLE_ENTITY, "시간이 유효하지 않습니다."),
    TIME_IN_USE(HttpStatus.CONFLICT, "해당 시간에 예약이 존재합니다.");

    private final HttpStatus status;
    private final String message;

    TimeErrorPolicy(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}
