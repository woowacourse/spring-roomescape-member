package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final Theme theme;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;

    private Reservation(Long id, String name, Theme theme, LocalDate reservationDate, ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public static Reservation of(Long id, String name, Theme theme, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, theme, date, time);
    }

    public static Reservation withoutId(String name, Theme theme, LocalDate reservationDate, ReservationTime reservationTime) {
        return new Reservation(null, name, theme, reservationDate, reservationTime);
    }

    public static Reservation assignId(Long id, Reservation reservation) {
        return new Reservation(id, reservation.getName(), reservation.getTheme(), reservation.getReservationDate(),
                reservation.getReservationTime());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public boolean isDuplicated(Reservation other) {
        return this.reservationDate.equals(other.reservationDate) && this.reservationTime.equals(other.reservationTime);
    }
}
