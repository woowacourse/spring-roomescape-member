package roomescape.exception.reservation;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationAlreadyExistsException extends BaseException {
    public ReservationAlreadyExistsException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
