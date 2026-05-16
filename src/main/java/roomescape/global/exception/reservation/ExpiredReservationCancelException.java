package roomescape.global.exception.reservation;

import roomescape.global.exception.status.ConflictException;

public class ExpiredReservationCancelException extends ConflictException {

    public ExpiredReservationCancelException(String message) {
        super(message);
    }
}
