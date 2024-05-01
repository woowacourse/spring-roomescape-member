package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private Long id;
    private final Name name;
    private final LocalDate date;
    private final Theme theme;
    private final ReservationTime reservationTime;

    public Reservation(Name name, LocalDate date, Theme theme, ReservationTime reservationTime) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
    }

    public Reservation(Long id, Name name, LocalDate date, Theme theme, ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return reservationTime;
    }
}
