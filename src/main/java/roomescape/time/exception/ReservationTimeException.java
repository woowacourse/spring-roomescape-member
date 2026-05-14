package roomescape.time.exception;

import roomescape.common.exception.ErrorCode;
import roomescape.common.exception.RoomEscapeException;

public class ReservationTimeException extends RoomEscapeException {
    public ReservationTimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
