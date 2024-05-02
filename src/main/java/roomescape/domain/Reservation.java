package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation from(Long id, String name, String date, ReservationTime time, Theme theme) {
        return new Reservation(id, new Name(name), ReservationDate.from(date), time, theme);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameAsString() {
        return name.asString();
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public String getDateAndTimeFormat() {
        return this.date.asString() + " " + this.time.getStartAtAsString();
    }

    public boolean isBefore(LocalDate localDate, LocalTime localTime) {
        if (this.date.isBefore(localDate)) {
            return true;
        }
        if (this.date.isEqual(localDate)) {
            return this.time.isBefore(localTime);
        }
        return false;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
