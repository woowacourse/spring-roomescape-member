package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class InvalidReservationTimeException extends RootBusinessException {

    private static final String MESSAGE = "예약은 10시~23시로만 가능합니다.";

    public InvalidReservationTimeException() {
        super(MESSAGE);
    }
}
