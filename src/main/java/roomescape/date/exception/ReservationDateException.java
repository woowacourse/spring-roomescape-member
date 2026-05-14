package roomescape.date.exception;

import roomescape.common.exception.ErrorCode;
import roomescape.common.exception.RoomEscapeException;

public class ReservationDateException extends RoomEscapeException {
    public ReservationDateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
