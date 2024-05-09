package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.model.RoomEscapeException;
import roomescape.name.domain.Name;
import roomescape.reservation.exception.ReservationExceptionCode;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public class Reservation {

    private long id;
    private final Name name;
    private final Date date;
    private final Time time;
    private final Theme theme;

    private Reservation(long id, String name, Date date, Time time, Theme theme) {
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private Reservation(String name, LocalDate date, long timeId, long themeId) {
        this(0, name, Date.saveDateFrom(date), new Time(timeId), new Theme(themeId));
    }

    public static Reservation reservationOf(long id, String name, LocalDate date, Time time, Theme theme) {
        return new Reservation(id, name, Date.dateFrom(date), time, theme);
    }

    public static Reservation saveReservationOf(String name, LocalDate date, long timeId, long themeId) {
        return new Reservation(name, date, timeId, themeId);
    }

    public static Reservation saveReservationOf(String name, LocalDate date, Time time, Theme theme) {
        validateAtSaveDateAndTime(date, time);
        return new Reservation(0, name, Date.dateFrom(date), time, theme);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public Time getReservationTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setIdOnSave(long id) {
        this.id = id;
    }

    private static void validateAtSaveDateAndTime(LocalDate date, Time time) {
        if (date.equals(LocalDate.now())) {
            validateTime(time);
        }
    }

    private static void validateTime(Time time) {
        if (time.isBeforeTime(LocalTime.now())) {
            throw new RoomEscapeException(ReservationExceptionCode.RESERVATION_TIME_IS_PAST_EXCEPTION);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
