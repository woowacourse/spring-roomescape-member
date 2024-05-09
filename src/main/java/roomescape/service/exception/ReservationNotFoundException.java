package roomescape.service.exception;

import roomescape.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException(final String message) {
        super(message);
    }
}
