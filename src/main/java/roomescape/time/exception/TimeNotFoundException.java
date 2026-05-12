package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class TimeNotFoundException extends BusinessException {

    public TimeNotFoundException() {
        super(TimeErrorPolicy.TIME_NOT_FOUND);
    }
}
