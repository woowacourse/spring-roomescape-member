package roomescape.reservation.exception;

import roomescape.global.exception.exception.ResourceNotFoundException;

public class ReservationResourceNotFoundException extends ResourceNotFoundException {
    public ReservationResourceNotFoundException() {
        super(ReservationErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }
}
