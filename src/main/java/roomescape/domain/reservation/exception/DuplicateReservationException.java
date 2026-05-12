package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class DuplicateReservationException extends BusinessException {

    public DuplicateReservationException() {
        super(ErrorCode.DUPLICATE_RESERVATION, null);
    }
}
