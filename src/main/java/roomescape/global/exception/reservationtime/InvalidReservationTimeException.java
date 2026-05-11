package roomescape.global.exception.reservationtime;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class InvalidReservationTimeException extends RoomescapeException {

    public InvalidReservationTimeException(String message) {
        super(ErrorCode.INVALID_RESERVATION_TIME, message);
    }
}
