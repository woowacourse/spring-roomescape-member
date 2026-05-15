package roomescape.exception.reservationtime;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationTimeAlreadyExistsException extends BaseException {
    public ReservationTimeAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
