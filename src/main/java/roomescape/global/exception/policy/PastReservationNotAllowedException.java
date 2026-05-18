package roomescape.global.exception.policy;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class PastReservationNotAllowedException extends ReservationException {

    public PastReservationNotAllowedException() {
        super(ErrorCode.RESERVATION_IN_PAST_NOT_ALLOWED, ErrorCode.RESERVATION_IN_PAST_NOT_ALLOWED.getMessage());
    }
}
