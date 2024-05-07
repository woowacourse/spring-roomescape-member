package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final Name name, final ReservationDate date, final ReservationTime time, final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation from(final Long id, final String name, final String date, final ReservationTime time, final Theme theme) {
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

    public String getLocalDateTimeFormat() {
        return parseLocalDateTime().toString();
    }

    public boolean isBefore(final LocalDate localDate, final LocalTime localTime) {
        return parseLocalDateTime().isBefore(LocalDateTime.of(localDate, localTime));
    }

    public LocalDateTime parseLocalDateTime() {
        return LocalDateTime.of(date.date(), this.time.getStartAt());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
