package roomescape.reservationtime.exception;

import java.util.List;
import roomescape.global.exception.exception.InvalidException;

public class ReservationTimeInvalidException extends InvalidException {
    public ReservationTimeInvalidException(List<String> errors) {
        super(errors);
    }
}
