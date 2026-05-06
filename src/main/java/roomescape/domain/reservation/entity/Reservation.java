package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import roomescape.domain.theme.entity.Theme;

public class Reservation {

    private Long id;

    private final String name;

    private final Theme theme;

    private final LocalDate date;

    private final ReservationTime time;

    public Reservation(Long id, String name, Theme theme, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Reservation(String name, Theme theme, LocalDate date, ReservationTime time) {
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
