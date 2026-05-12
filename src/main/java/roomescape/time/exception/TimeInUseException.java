package roomescape.time.exception;

import roomescape.global.BusinessException;

public class TimeInUseException extends BusinessException {

    public TimeInUseException() {
        super(TimeErrorPolicy.TIME_IN_USE);
    }
}
