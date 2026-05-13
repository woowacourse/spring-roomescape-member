package roomescape.reservation.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidReservationDateValueException extends BusinessException {

    public InvalidReservationDateValueException() {
        super(ReservationErrorPolicy.INVALID_RESERVATION_DATE_VALUE);
    }
}
