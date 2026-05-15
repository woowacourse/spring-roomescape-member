package roomescape.reservationtime.exception;

import roomescape.global.exception.exception.ResourceInUseException;

public class ReservationTimeInUseException extends ResourceInUseException {
    public ReservationTimeInUseException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_CONSTRAINT.getMessage());
    }
}
