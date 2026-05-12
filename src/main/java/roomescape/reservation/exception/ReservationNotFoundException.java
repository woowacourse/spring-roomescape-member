package roomescape.reservation.exception;

import roomescape.global.BusinessException;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException() {
        super(ReservationErrorCode.RESERVATION_NOT_FOUND);
    }
}
