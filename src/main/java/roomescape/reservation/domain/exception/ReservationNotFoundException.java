package roomescape.reservation.domain.exception;

import roomescape.common.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
