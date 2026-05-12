package roomescape.reservation.exception;

import roomescape.global.BusinessException;

public class DuplicateReservationException extends BusinessException {

    public DuplicateReservationException() {
        super(ReservationErrorCode.DUPLICATE_RESERVATION);
    }
}
