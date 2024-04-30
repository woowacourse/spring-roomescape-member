package roomescape.domain;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Name name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Long id() {
        return id;
    } // TODO 네이밍 변경 고려

    public Name name() {
        return name;
    }

    public LocalDate date() {
        return date;
    }

    public String date(DateTimeFormatter formatter) {
        return date.format(formatter);
    }

    public ReservationTime time() {
        return time;
    }

    public Theme getTheme() {
        return theme;
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
