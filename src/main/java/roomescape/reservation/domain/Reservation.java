package roomescape.reservation.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private final ReservationName reservationName;
    private final LocalDate date;
    private final Theme theme;
    private final ReservationTime reservationTime;

    public Reservation(ReservationName reservationName, LocalDate date, Theme theme, ReservationTime reservationTime) {
        this.reservationName = reservationName;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
    }

    public Reservation(Long id, ReservationName reservationName, LocalDate date, Theme theme, ReservationTime reservationTime) {
        this.id = id;
        this.reservationName = reservationName;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return reservationName.getName();
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
