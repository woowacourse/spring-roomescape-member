package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class TimeInUseException extends BusinessException {

    public TimeInUseException() {
        super(TimeErrorPolicy.TIME_IN_USE);
    }
}
