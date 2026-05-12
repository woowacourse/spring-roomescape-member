package roomescape.reservation.exception;

import roomescape.global.BusinessException;

public class InvalidReservationRequestException extends BusinessException {

    public InvalidReservationRequestException() {
        super(ReservationErrorCode.INVALID_RESERVATION_REQUEST);
    }
}
