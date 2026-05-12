package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;

    CommonErrorCode(HttpStatus status, String message) {
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
