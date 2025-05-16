package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.user.domain.User;

public class Reservation {
    private final Long id;
    private final User user;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, User user, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(User user, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, user, date, reservationTime, theme);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
