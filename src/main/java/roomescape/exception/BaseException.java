package roomescape.exception;

import roomescape.exception.code.ErrorCode;

public class BaseException extends RuntimeException {
    private final ErrorCode code;

    public BaseException(ErrorCode code) {
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return code.getMessage();
    }

}
