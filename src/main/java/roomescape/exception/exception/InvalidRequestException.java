package roomescape.exception.exception;

import roomescape.exception.dto.ErrorCode;

public class InvalidRequestException extends BaseCustomException {
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
