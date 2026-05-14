package roomescape.global.exception.policy;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class ReservationUpdateNotAllowedException extends ReservationException {

    public ReservationUpdateNotAllowedException() {
        super(ErrorCode.CANCEL_IN_PAST_NOT_ALLOWED, ErrorCode.CANCEL_IN_PAST_NOT_ALLOWED.getMessage());
    }

    public ReservationUpdateNotAllowedException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
