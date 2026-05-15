package roomescape.common.exception;

import roomescape.common.ErrorCode;

public class NotFoundException extends RestApiException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }

    public NotFoundException(ErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, detailMessage, cause);
    }

    public NotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
