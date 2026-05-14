package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ScheduleTimeInUseException extends BaseException {
    public ScheduleTimeInUseException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
