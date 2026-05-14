package roomescape.global.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidRequestFormatException extends BusinessException {

    public InvalidRequestFormatException() {
        super(CommonErrorPolicy.INVALID_REQUEST_FORMAT);
    }
}
