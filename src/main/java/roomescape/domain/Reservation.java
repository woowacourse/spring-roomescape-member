package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = new Name(name);
        this.date = new ReservationDate(date);
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public boolean isDateBefore(LocalDate target) {
        return getDate().isBefore(target);
    }

    public Long getId() {
        return id;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public String getName() {
        return name.getName();
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
