package roomescape.time.exception;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;

public class TimeException extends RoomescapeException {
    public TimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
