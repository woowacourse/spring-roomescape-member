package roomescape.service.reservation.exception;

import roomescape.exception.ConflictException;

public class ReservationDuplicatedException extends ConflictException {

    public ReservationDuplicatedException(String message) {
        super(message);
    }
}
