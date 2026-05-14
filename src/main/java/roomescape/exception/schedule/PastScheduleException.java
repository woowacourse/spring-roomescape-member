package roomescape.exception.schedule;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class PastScheduleException extends BaseException {
    public PastScheduleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
