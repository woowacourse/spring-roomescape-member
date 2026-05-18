package roomescape.domain.time.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class ReservationTimeNotFoundException extends BusinessException {

    public ReservationTimeNotFoundException() {
        super(ErrorCode.RESERVATION_TIME_NOT_FOUND);
    }
}
