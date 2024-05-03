package roomescape.service.exception;

import roomescape.exception.RoomescapeException;

public class DuplicateReservation extends RoomescapeException {

    public DuplicateReservation(final String message) {
        super(message);
    }
}
