package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.member.Name;
import roomescape.domain.theme.Theme;

public class Reservation {

    private final Long id;
    private final Name name;
    private final Date date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String rawName, String rawDate, ReservationTime time, Theme theme) {
        this(null, rawName, rawDate, time, theme);
    }

    public Reservation(Long id, String rawName, String rawDate, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = new Name(rawName);
        this.date = new Date(rawDate);
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationTime getTime() {
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
            && Objects.equals(date, that.date) && Objects.equals(time, that.time)
            && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time, theme);
    }
}
