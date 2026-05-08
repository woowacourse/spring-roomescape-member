package roomescape.reservation.exception;

import java.util.List;
import roomescape.exception.exception.InvalidException;

public class InvalidReservationException extends InvalidException {
    public InvalidReservationException(List<String> errors) {
        super(errors);
    }
}
