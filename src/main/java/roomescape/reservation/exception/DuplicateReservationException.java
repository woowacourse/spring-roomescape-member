package roomescape.reservation.exception;

import roomescape.global.exception.base.BusinessException;

public class DuplicateReservationException extends BusinessException {

    public DuplicateReservationException() {
        super(ReservationErrorPolicy.DUPLICATE_RESERVATION);
    }
}
