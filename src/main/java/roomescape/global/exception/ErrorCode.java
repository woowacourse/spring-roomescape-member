package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "입력값이 적절하지 않습니다."),
    INVALID_REQUEST_FORMAT("INVALID_REQUEST_FORMAT", HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),;

    private final String errorCode;
    private final HttpStatus status;
    private final String message;

    ErrorCode(final String errorCode, final HttpStatus status, final String message) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
    }

    public String errorCode() {
        return errorCode;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
