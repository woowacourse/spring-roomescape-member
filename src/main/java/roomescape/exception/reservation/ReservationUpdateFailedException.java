package roomescape.exception.reservation;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationUpdateFailedException extends BaseException {
    public ReservationUpdateFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
