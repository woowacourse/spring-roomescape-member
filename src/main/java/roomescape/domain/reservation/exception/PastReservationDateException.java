package roomescape.domain.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class PastReservationDateException extends BusinessException {

    public PastReservationDateException() {
        super(ErrorCode.PAST_DATE_RESERVATION);
    }
}
