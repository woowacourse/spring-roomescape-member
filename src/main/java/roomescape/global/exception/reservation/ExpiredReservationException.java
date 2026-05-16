package roomescape.global.exception.reservation;

import roomescape.global.exception.status.ConflictException;

public class ExpiredReservationException extends ConflictException {

    public ExpiredReservationException(String message) {
        super(message);
    }
}
