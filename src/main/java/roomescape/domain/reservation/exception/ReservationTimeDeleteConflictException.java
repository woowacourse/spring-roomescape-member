package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class ReservationTimeDeleteConflictException extends BusinessException {

    public ReservationTimeDeleteConflictException(Throwable cause) {
        super(ErrorCode.RESERVATION_TIME_DELETE_CONFLICT, cause);
    }
}
