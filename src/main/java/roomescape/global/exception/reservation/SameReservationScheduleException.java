package roomescape.global.exception.reservation;

import roomescape.global.exception.status.ConflictException;

public class SameReservationScheduleException extends ConflictException {

    public SameReservationScheduleException(String message) {
        super(message);
    }
}
