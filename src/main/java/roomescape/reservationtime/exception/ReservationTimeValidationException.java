package roomescape.reservationtime.exception;

import java.util.List;
import roomescape.global.exception.exception.ValidationException;

public class ReservationTimeValidationException extends ValidationException {
    public ReservationTimeValidationException(List<String> errors) {
        super(errors);
    }
}
