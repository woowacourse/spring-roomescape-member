package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidTimeStartAtValueException extends BusinessException {

    public InvalidTimeStartAtValueException() {
        super(TimeErrorPolicy.INVALID_TIME_START_AT_VALUE);
    }
}
