package roomescape.reservation.domain.exception;

import roomescape.common.exception.AlreadyInUseException;

public class ReservationInUseException extends AlreadyInUseException {
    public ReservationInUseException(String message) {
        super(message);
    }
}
