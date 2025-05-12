package roomescape.model;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final User user;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, LocalDate date, User user, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public boolean isPast(LocalDate other) {
        return !date.isAfter(other);
    }

    public boolean isSameTime(ReservationTime other) {
        return reservationTime.isSameTime(other);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return user.getNameValue();
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
