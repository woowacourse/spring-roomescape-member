package roomescape.date.exception;

import roomescape.common.exception.ExceptionInformation;
import roomescape.common.exception.RoomEscapeException;

public class ReservationDateException extends RoomEscapeException {
    public ReservationDateException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
