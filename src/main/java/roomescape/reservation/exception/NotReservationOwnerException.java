package roomescape.reservation.exception;

import roomescape.global.exception.base.BusinessException;

public class NotReservationOwnerException extends BusinessException {

    public NotReservationOwnerException() {
        super(ReservationErrorPolicy.NOT_RESERVATION_OWNER);
    }
}
