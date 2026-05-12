package roomescape.reservation.exception;

import java.util.List;
import roomescape.global.exception.exception.InvalidException;

public class InvalidReservationException extends InvalidException {
    public InvalidReservationException(List<String> errors) {
        super(errors);
    }
}
