package roomescape.common.exception;

import roomescape.common.ErrorCode;

public class BadRequestException extends RestApiException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }

    public BadRequestException(ErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, detailMessage, cause);
    }

    public BadRequestException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
