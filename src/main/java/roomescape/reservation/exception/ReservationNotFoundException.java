package roomescape.reservation.exception;

import roomescape.exception.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException() {
        super(ReservationErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }
}
