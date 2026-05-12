package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidTimeStartAtException extends BusinessException {

    public InvalidTimeStartAtException() {
        super(TimeErrorPolicy.INVALID_TIME_START_AT);
    }
}
