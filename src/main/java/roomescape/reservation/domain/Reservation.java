package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Long themeId;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Long themeId) {
        this(null, name, date, time, themeId, null);
    }

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Long themeId, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
        this.theme = theme;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.name, this.date, this.time, this.themeId, this.theme);
    }

    public Reservation withTheme(Theme theme) {
        return new Reservation(this.id, this.name, this.date, this.time, this.themeId, theme);
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

    public ReservationTime getTime() {
        return time;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Theme getTheme() {
        return theme;
    }
}
