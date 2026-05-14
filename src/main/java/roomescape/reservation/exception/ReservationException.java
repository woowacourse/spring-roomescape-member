package roomescape.reservation.exception;

import roomescape.common.exception.ExceptionInformation;
import roomescape.common.exception.RoomEscapeException;

public class ReservationException extends RoomEscapeException {
    public ReservationException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
