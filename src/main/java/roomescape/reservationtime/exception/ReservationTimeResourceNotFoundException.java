package roomescape.reservationtime.exception;

import roomescape.global.exception.exception.ResourceNotFoundException;

public class ReservationTimeResourceNotFoundException extends ResourceNotFoundException {
    public ReservationTimeResourceNotFoundException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }
}
