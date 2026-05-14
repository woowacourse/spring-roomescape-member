package roomescape.reservation.exception;

import roomescape.common.exception.ErrorCode;
import roomescape.common.exception.RoomEscapeException;

public class ReservationException extends RoomEscapeException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
