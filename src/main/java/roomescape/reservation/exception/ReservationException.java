package roomescape.reservation.exception;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;

public class ReservationException extends RoomescapeException {

    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReservationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
