package roomescape.exception.reservationtime;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationTimeNotFoundException extends BaseException {
    public ReservationTimeNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
