package roomescape.reservation.exception;

import roomescape.global.exception.exception.BadRequestException;

public class ReservationBadRequestException extends BadRequestException {
    public ReservationBadRequestException(String message) {
        super(message);
    }
}
