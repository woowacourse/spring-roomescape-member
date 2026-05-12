package roomescape.reservation.exception;

import roomescape.global.BusinessException;

public class InvalidReservationDateException extends BusinessException {

    public InvalidReservationDateException() {
        super(ReservationErrorCode.INVALID_RESERVATION_DATE);
    }
}
