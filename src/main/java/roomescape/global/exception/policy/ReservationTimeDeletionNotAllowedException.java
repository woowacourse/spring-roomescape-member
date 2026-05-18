package roomescape.global.exception.policy;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class ReservationTimeDeletionNotAllowedException extends ReservationException {

    public ReservationTimeDeletionNotAllowedException() {
        super(ErrorCode.RESERVATION_TIME_DELETE_NOT_ALLOWED, ErrorCode.RESERVATION_TIME_DELETE_NOT_ALLOWED.getMessage());
    }
}
