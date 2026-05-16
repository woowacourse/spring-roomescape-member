package roomescape.global.exception.reservationtime;

import roomescape.global.exception.status.ConflictException;

public class ReservationTimeInUseException extends ConflictException {
    public ReservationTimeInUseException(String message) {
        super(message);
    }
}
