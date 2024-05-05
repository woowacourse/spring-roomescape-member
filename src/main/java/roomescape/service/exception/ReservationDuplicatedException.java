package roomescape.service.exception;

import roomescape.exception.DuplicateException;

public class ReservationDuplicatedException extends DuplicateException {

    public ReservationDuplicatedException(final String message) {
        super(message);
    }
}
