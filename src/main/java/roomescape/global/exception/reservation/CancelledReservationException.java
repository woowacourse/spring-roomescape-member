package roomescape.global.exception.reservation;

import roomescape.global.exception.status.ConflictException;

public class CancelledReservationException extends ConflictException {
    public CancelledReservationException(String message) {
        super(message);
    }
}
