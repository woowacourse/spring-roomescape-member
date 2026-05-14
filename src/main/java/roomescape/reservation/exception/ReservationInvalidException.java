package roomescape.reservation.exception;

import java.util.List;
import roomescape.global.exception.exception.InvalidException;

public class ReservationInvalidException extends InvalidException {
    public ReservationInvalidException(List<String> errors) {
        super(errors);
    }
}
