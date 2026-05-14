package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ScheduleDeleteFailedException extends BaseException {
    public ScheduleDeleteFailedException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
