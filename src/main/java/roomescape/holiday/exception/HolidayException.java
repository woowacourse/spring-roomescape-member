package roomescape.holiday.exception;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;

public class HolidayException extends RoomescapeException {
    public HolidayException(ErrorCode errorCode) {
        super(errorCode);
    }
}
