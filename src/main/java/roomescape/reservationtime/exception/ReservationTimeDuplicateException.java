package roomescape.reservationtime.exception;

import roomescape.exception.exception.DuplicateException;

public class ReservationTimeDuplicateException extends DuplicateException {

    public ReservationTimeDuplicateException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_DUPLICATE.getMessage());
    }
}
