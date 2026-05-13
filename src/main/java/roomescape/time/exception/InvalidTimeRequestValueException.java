package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidTimeRequestValueException extends BusinessException {

    public InvalidTimeRequestValueException() {
        super(TimeErrorPolicy.INVALID_TIME_REQUEST_VALUE);
    }
}
