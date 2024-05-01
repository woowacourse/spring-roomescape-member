package roomescape.domain.reservation;

import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, Time time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
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
