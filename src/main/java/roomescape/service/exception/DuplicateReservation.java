package roomescape.service.exception;

import roomescape.exception.CustomException;

public class DuplicateReservation extends CustomException {

    public DuplicateReservation(final String message) {
        super(message);
    }
}
