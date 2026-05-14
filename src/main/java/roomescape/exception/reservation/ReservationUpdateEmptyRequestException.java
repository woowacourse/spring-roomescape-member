package roomescape.exception.reservation;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationUpdateEmptyRequestException extends BaseException {
    public ReservationUpdateEmptyRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
