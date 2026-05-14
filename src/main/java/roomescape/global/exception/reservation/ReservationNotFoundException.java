package roomescape.global.exception.reservation;

import roomescape.global.exception.status.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
