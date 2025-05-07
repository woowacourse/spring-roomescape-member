package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public abstract class RoomescapeException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;

    public RoomescapeException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
