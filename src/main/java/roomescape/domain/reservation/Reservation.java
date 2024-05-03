package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.ReservationTime;
import roomescape.domain.theme.Theme;

public class Reservation {

    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, name, date, reservationTime, theme);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.name();
    }


    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
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
