package roomescape.global.exception.reservationtime;

import roomescape.global.exception.status.NotFoundException;

public class ReservationTimeNotFoundException extends NotFoundException {
    public ReservationTimeNotFoundException(String message) {
        super(message);
    }
}
