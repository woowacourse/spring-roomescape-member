package roomescape.time.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.base.ErrorPolicy;

public enum TimeErrorPolicy implements ErrorPolicy {
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "시간이 존재하지 않습니다."),
    DUPLICATE_TIME(HttpStatus.CONFLICT, "시간이 이미 존재합니다."),
    INVALID_TIME_START_AT(HttpStatus.UNPROCESSABLE_ENTITY, "시간이 유효하지 않습니다."),
    TIME_IN_USE(HttpStatus.CONFLICT, "해당 시간에 예약이 존재합니다."),
    INVALID_TIME_REQUEST(HttpStatus.BAD_REQUEST, "시간 등록 요청 값이 유효하지 않습니다.");

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
