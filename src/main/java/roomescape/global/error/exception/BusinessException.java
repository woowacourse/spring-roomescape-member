package roomescape.global.error.exception;

import roomescape.global.error.ErrorCode;
import roomescape.global.error.ErrorDetail;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final ErrorDetail error;

    public BusinessException(ErrorCode errorCode, ErrorDetail error) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.error = error;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.error = null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ErrorDetail getError() {
        return error;
    }
}
