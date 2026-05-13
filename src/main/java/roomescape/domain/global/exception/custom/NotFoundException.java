package roomescape.domain.global.exception.custom;

import roomescape.domain.global.exception.error.ErrorCode;

public class NotFoundException extends RuntimeException implements BaseException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
