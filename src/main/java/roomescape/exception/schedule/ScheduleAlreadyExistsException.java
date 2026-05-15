package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ScheduleAlreadyExistsException extends BaseException {
    public ScheduleAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
