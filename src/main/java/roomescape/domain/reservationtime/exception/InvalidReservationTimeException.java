package roomescape.domain.reservationtime.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class InvalidReservationTimeException extends BusinessException {

    public InvalidReservationTimeException() {
        super(ErrorCode.INVALID_RESERVATION_TIME, null);
    }
}
