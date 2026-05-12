package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class InvalidReservationException extends BusinessException {

    public InvalidReservationException() {
        super(ErrorCode.INVALID_RESERVATION, null);
    }
}
