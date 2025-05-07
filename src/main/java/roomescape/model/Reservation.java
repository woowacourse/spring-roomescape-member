package roomescape.model;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final UserName name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, UserName name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
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

    public String getName() {
        return name.getName();
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
