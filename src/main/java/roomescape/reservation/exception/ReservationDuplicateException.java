package roomescape.reservation.exception;

import roomescape.global.exception.EntityDuplicateException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDuplicateException extends EntityDuplicateException {

    public ReservationDuplicateException(String message, LocalDate date, LocalTime time, String themeName) {
        super("[ERROR] " + message + " " + date + " " + time + " " + themeName);
    }
}
