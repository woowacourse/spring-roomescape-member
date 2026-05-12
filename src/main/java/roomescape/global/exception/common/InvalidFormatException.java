package roomescape.global.exception.common;

import roomescape.global.exception.base.BusinessException;

public class InvalidFormatException extends BusinessException {

    public InvalidFormatException() {
        super(CommonErrorPolicy.INVALID_FORMAT);
    }
}
