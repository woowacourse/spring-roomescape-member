package roomescape.service.reservation.exception;

import roomescape.exception.BadRequestException;

public class PreviousTimeException extends BadRequestException {

    public PreviousTimeException(String message) {
        super(message);
    }
}
