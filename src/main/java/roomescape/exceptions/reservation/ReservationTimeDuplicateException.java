package roomescape.exceptions.reservation;

import java.time.LocalTime;
import roomescape.exceptions.EntityDuplicateException;

public class ReservationTimeDuplicateException extends EntityDuplicateException {

    public ReservationTimeDuplicateException(String message, LocalTime time) {
        super(message + " " + time);
    }
}
