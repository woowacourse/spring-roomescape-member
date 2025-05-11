package roomescape.time.exception;

import roomescape.global.exception.EntityDuplicateException;

import java.time.LocalTime;

public class ReservationTimeDuplicateException extends EntityDuplicateException {

    public ReservationTimeDuplicateException(String message, LocalTime time) {
        super("[ERROR] " + message + " " + time);
    }
}
