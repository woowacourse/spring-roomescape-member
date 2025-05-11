package roomescape.reservationtime.exception;

import roomescape.global.exception.NotFoundException;

public class ReservationTimeNotFoundException extends NotFoundException {

    public ReservationTimeNotFoundException(final String message) {
        super(message);
    }
}
