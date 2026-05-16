package roomescape.global.exception.reservation;

import roomescape.global.exception.status.ConflictException;

public class ExpiredReservationChangeException extends ConflictException {

    public ExpiredReservationChangeException(String message) {
        super(message);
    }
}
