package roomescape.exception.reservation;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ReservationInternalServerErrorException extends BaseException {
    public ReservationInternalServerErrorException(ErrorCode errorCode) {
        super(errorCode);
    }
}
