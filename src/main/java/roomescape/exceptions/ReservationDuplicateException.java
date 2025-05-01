package roomescape.exceptions;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDuplicateException extends EntityDuplicateException {

    private final LocalDate date;
    private final LocalTime time;
    private final String themeName;

    public ReservationDuplicateException(String message, LocalDate date, LocalTime time, String themeName) {
        super(String.format("%s %s %s %s", message, date, time, themeName));
        this.date = date;
        this.time = time;
        this.themeName = themeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getThemeName() {
        return themeName;
    }
}
