package roomescape.exception.impl;

import roomescape.exception.RootException;

public class InvalidReservationTimeException extends RootException {

    private static final String MESSAGE = "예약은 10시~23시로만 가능합니다.";

    public InvalidReservationTimeException() {
        super(MESSAGE);
    }
}
