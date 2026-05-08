package roomescape.reservationtime.exception;

import roomescape.exception.exception.NotFoundException;

public class ReservationTimeNotFoundException extends NotFoundException {
    public ReservationTimeNotFoundException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }
}
