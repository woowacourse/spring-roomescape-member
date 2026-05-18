package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }
}
