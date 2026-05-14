package roomescape.exception.reservation;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationNotFoundException extends BaseException {
    public ReservationNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
