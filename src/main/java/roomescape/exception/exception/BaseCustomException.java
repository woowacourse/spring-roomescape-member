package roomescape.exception.exception;

import roomescape.exception.dto.ErrorCode;

public class BaseCustomException extends RuntimeException implements CustomException {
    private final ErrorCode errorCode;

    public BaseCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
