package roomescape.reservation.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidReservationRequestValueException extends BusinessException {

    public InvalidReservationRequestValueException() {
        super(ReservationErrorPolicy.INVALID_RESERVATION_REQUEST_VALUE);
    }
}
