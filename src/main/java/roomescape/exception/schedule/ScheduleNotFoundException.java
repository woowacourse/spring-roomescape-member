package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ScheduleNotFoundException extends BaseException {
    public ScheduleNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
