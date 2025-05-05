package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(final String name,
                       final LocalDate date,
                       final ReservationTime time,
                       final ReservationTheme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation assignId(final Long id) {
        this.id = id;
        return this;
    }

    public boolean isDuplicateReservation(Reservation reservation) {
        return this.date.equals(reservation.date) && this.time.isSameTime(reservation.time);
    }

    public long getId() {
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

    public ReservationTheme getTheme() {
        return theme;
    }
}
