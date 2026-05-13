package roomescape.global.exception.common;

import roomescape.global.exception.base.BusinessException;

public class InvalidRequestValueException extends BusinessException {

    public InvalidRequestValueException() {
        super(CommonErrorPolicy.INVALID_REQUEST_VALUE);
    }
}
