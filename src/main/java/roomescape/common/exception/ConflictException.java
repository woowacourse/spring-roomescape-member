package roomescape.common.exception;

import roomescape.common.ErrorCode;

public class ConflictException extends RestApiException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ConflictException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }

    public ConflictException(ErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, detailMessage, cause);
    }

    public ConflictException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
