package roomescape.reservation.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidReservationRequestException extends BusinessException {

    public InvalidReservationRequestException() {
        super(ReservationErrorPolicy.INVALID_RESERVATION_REQUEST);
    }
}
