package roomescape.reservation.exception;

import roomescape.global.BusinessException;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException() {
        super(ReservationErrorPolicy.RESERVATION_NOT_FOUND);
    }
}
