package roomescape.reservation.exception;

import roomescape.exception.exception.BadRequestException;

public class ReservationBadRequestException extends BadRequestException {
    public ReservationBadRequestException(String message) {
        super(message);
    }
}
