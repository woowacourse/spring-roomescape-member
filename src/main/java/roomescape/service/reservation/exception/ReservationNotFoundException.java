package roomescape.service.reservation.exception;

import roomescape.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
