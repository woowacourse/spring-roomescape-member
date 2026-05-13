package roomescape.global.exception.common;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.base.ErrorPolicy;

public enum CommonErrorPolicy implements ErrorPolicy {
    INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST,  "요청 본문 형식이 유효하지 않습니다."),
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST,  "요청 값이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    CommonErrorPolicy(HttpStatus status, String message) {
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
