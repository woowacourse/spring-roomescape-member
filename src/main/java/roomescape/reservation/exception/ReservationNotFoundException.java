package roomescape.reservation.exception;

import roomescape.global.exception.base.BusinessException;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException() {
        super(ReservationErrorPolicy.RESERVATION_NOT_FOUND);
    }
}
