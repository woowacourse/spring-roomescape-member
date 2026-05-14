package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ScheduleThemeInUseException extends BaseException {
    public ScheduleThemeInUseException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
