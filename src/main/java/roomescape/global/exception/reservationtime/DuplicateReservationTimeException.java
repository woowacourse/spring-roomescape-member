package roomescape.global.exception.reservationtime;

import roomescape.global.exception.status.ConflictException;

public class DuplicateReservationTimeException extends ConflictException {
    public DuplicateReservationTimeException(String message) {
        super(message);
    }
}
