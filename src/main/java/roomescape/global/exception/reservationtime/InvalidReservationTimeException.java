package roomescape.global.exception.reservationtime;

import roomescape.global.exception.status.BadRequestException;

public class InvalidReservationTimeException extends BadRequestException {

    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
