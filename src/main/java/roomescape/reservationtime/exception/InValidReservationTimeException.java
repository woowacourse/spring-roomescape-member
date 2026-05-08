package roomescape.reservationtime.exception;

import java.util.List;
import roomescape.exception.exception.InvalidException;

public class InValidReservationTimeException extends InvalidException {
    public InValidReservationTimeException(List<String> errors) {
        super(errors);
    }
}
