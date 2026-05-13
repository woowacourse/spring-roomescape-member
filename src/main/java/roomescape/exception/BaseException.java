package roomescape.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final ErrorCode code;

    public BaseException(ErrorCode code) {
        this.code = code;
    }

    public String getAction() {
        return code.getAction();
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return code.getMessage();
    }

    public HttpStatus getStatus() {
        return code.getStatus();
    }
}
