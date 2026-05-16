package roomescape.global.exception.reservation;

import roomescape.global.exception.status.ConflictException;

public class DuplicateReservationException extends ConflictException {
    public DuplicateReservationException(String message) {
        super(message);
    }
}
