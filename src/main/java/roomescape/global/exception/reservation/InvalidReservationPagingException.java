package roomescape.global.exception.reservation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class InvalidReservationPagingException extends RoomescapeException {

    public InvalidReservationPagingException(String message) {
        super(ErrorCode.INVALID_RESERVATION_PAGING, message);
    }
}
