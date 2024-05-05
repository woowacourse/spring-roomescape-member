package roomescape.service.exception;

import roomescape.exception.ConflictException;

public class ReservationDuplicatedException extends ConflictException {

    public ReservationDuplicatedException(final String message) {
        super(message);
    }
}
