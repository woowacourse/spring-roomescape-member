package roomescape.reservation.exception;

import roomescape.global.exception.exception.DuplicateException;

public class ReservationDuplicateException extends DuplicateException {
    public ReservationDuplicateException() {
        super(ReservationErrorCode.RESERVATION_DUPLICATE.getMessage());
    }
}
