package roomescape.reservationTime.exception;

import roomescape.globalException.ConflictException;

public class DuplicateReservationException extends ConflictException {
    public DuplicateReservationException(String message) {
        super(message);
    }
}
