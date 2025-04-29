package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final long id,
                       final String name,
                       final LocalDate date,
                       final ReservationTime time,
                       final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name,
                       final LocalDate date,
                       final ReservationTime reservationTime,
                       final Theme theme) {
        this(0, name, date, reservationTime, theme);
    }

    public Reservation withId(final long id) {
        return new Reservation(id, this.name, this.date, this.time, this.theme);
    }

    public boolean isSameDateTime(LocalDate date, long timeId) {
        return this.date.equals(date) && this.time.getId() == timeId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
