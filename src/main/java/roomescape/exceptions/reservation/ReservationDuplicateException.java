package roomescape.exceptions.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.exceptions.EntityDuplicateException;

public class ReservationDuplicateException extends EntityDuplicateException {

    public ReservationDuplicateException(String message, LocalDate date, LocalTime time, String themeName) {
        super(String.format("%s %s %s %s", message, date, time, themeName));
    }
}
