package roomescape.exceptions;

import java.time.LocalTime;

public class ReservationTimeDuplicateException extends EntityDuplicateException {

    public ReservationTimeDuplicateException(String message, LocalTime time) {
        super(message + " " + time);
    }
}
