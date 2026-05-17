package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ScheduleInUseException extends BaseException {
    public ScheduleInUseException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
