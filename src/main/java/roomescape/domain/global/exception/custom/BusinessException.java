package roomescape.domain.global.exception.custom;

import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final ErrorDetail error;

    public BusinessException(ErrorCode errorCode, ErrorDetail error) {
        this.errorCode = errorCode;
        this.error = error;
    }

    public BusinessException(ErrorCode errorCode) {
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
