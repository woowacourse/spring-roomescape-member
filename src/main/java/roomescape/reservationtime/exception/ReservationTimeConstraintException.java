package roomescape.reservationtime.exception;

import roomescape.global.exception.exception.ForeignKeyConstraintException;

public class ReservationTimeConstraintException extends ForeignKeyConstraintException {
    public ReservationTimeConstraintException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_CONSTRAINT.getMessage());
    }
}
