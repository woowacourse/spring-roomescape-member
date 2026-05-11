package roomescape.global.exception.reservation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class InvalidReservationException extends RoomescapeException {

    public InvalidReservationException(String message) {
        super(ErrorCode.INVALID_RESERVATION, message);
    }
}
