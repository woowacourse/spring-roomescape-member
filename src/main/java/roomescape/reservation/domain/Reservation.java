package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.name, this.date, this.time, this.theme);
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

    public Theme getTheme() {
        return theme;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public boolean hasTimeId(Long timeId) {
        return time.isSameId(timeId);
    }

    public boolean hasThemeId(Long themeId) {
        return theme.isSameId(themeId);
    }

    public boolean hasSameDate(LocalDate date) {
        return this.date.isEqual(date);
    }

}
