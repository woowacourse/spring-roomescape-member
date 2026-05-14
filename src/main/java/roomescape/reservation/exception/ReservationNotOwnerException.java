package roomescape.reservation.exception;

import roomescape.global.exception.exception.ForbiddenException;

public class ReservationNotOwnerException extends ForbiddenException {
    public ReservationNotOwnerException(String message) {
        super(message);
    }
}
