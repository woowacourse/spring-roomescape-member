package roomescape.exceptions;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDuplicateException extends RuntimeException {
    public ReservationDuplicateException(String message, LocalDate date, LocalTime time) {
        super("[ERROR] " + message + " " + date + " " + time);
    }
}
