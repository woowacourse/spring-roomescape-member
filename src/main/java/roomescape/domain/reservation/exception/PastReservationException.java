package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class PastReservationException extends BusinessException {

    public PastReservationException() {
        super(ErrorCode.PAST_RESERVATION, null);
    }
}
