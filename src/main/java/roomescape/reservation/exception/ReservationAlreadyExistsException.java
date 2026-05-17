package roomescape.reservation.exception;

import roomescape.global.exception.exception.AlreadyExistsException;

public class ReservationAlreadyExistsException extends AlreadyExistsException {
    public ReservationAlreadyExistsException() {
        super(ReservationErrorCode.RESERVATION_DUPLICATE.getMessage());
    }
}
