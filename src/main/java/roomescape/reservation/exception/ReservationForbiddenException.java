package roomescape.reservation.exception;

import roomescape.global.exception.exception.ForbiddenException;

public class ReservationForbiddenException extends ForbiddenException {
    public ReservationForbiddenException(String message) {
        super(message);
    }
}
