package roomescape.reservationtime.exception;

import roomescape.global.exception.exception.AlreadyExistsException;

public class ReservationTimeAlreadyExistsException extends AlreadyExistsException {

    public ReservationTimeAlreadyExistsException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_DUPLICATE.getMessage());
    }
}
