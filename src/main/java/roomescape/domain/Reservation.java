package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, Time time, Theme theme) {
        this(null, name, date, time, theme);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(time);
        result = 31 * result + Objects.hashCode(theme);
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Reservation that)) {
            return false;
        }

        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(date, that.date) && Objects.equals(time, that.time)
                && Objects.equals(theme, that.theme);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
