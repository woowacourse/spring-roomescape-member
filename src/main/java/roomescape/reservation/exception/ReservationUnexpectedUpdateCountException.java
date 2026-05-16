package roomescape.reservation.exception;

import roomescape.global.exception.exception.UnexpectedUpdateCountException;

public class ReservationUnexpectedUpdateCountException extends UnexpectedUpdateCountException {
    public ReservationUnexpectedUpdateCountException(int updateRowCount) {
        super(updateRowCount);
    }
}
