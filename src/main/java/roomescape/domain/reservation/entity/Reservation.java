package roomescape.domain.reservation.entity;

import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.ReservationTime;

import java.time.LocalDate;

public class Reservation {

    private Long id;

    private final String username;

    private final Theme theme;

    private final LocalDate date;

    private final ReservationTime time;

    public Reservation(Long id, String username, Theme theme, LocalDate date, ReservationTime time) {
        this.id = id;
        this.username = username;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Reservation(String username, Theme theme, LocalDate date, ReservationTime time) {
        this.username = username;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
