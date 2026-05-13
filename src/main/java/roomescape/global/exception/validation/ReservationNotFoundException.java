package roomescape.global.exception.validation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class ReservationNotFoundException extends ReservationException {

    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }
}
