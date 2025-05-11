package roomescape.time.application;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class TimeNotFoundException extends BusinessException {

    public TimeNotFoundException() {
        super(ErrorCode.TIME_NOT_FOUND);
    }
}
