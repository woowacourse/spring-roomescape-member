package roomescape.global.exception.policy;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class PastReservationCancelNotAllowedException extends ReservationException {

    public PastReservationCancelNotAllowedException() {
        super(ErrorCode.CANCEL_IN_PAST_NOT_ALLOWED, ErrorCode.CANCEL_IN_PAST_NOT_ALLOWED.getMessage());
    }
}
