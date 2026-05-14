package roomescape.time.exception;

import roomescape.common.exception.ExceptionInformation;
import roomescape.common.exception.RoomEscapeException;

public class ReservationTimeException extends RoomEscapeException {
    public ReservationTimeException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
