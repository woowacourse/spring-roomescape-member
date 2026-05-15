package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class ReservationOwnerMismatchException extends BusinessException {

    public ReservationOwnerMismatchException() {
        super(ErrorCode.RESERVATION_OWNER_MISMATCH);
    }
}
