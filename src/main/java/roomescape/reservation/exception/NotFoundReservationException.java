package roomescape.reservation.exception;

import roomescape.globalException.NotFoundException;

public class NotFoundReservationException extends NotFoundException {

    public NotFoundReservationException(String message) {
        super(message);
    }
}
