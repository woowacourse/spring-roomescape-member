package roomescape.reservationtime.exception;

import roomescape.exception.exception.ForeignKeyConstraintException;

public class ReservationTimeConstraintException extends ForeignKeyConstraintException {
    public ReservationTimeConstraintException(String message) {
        super(message);
    }
}
