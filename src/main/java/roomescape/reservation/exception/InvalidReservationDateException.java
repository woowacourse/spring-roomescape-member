package roomescape.reservation.exception;

import roomescape.global.BusinessException;

public class InvalidReservationDateException extends BusinessException {

    public InvalidReservationDateException() {
        super(ReservationErrorPolicy.INVALID_RESERVATION_DATE);
    }
}
