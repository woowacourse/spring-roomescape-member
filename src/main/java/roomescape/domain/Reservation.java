package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;

    public Reservation(Long id, String name, LocalDate reservationDate, ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, date, time);
    }

    public static Reservation withoutId(String name, LocalDate reservationDate, ReservationTime reservationTime) {
        return new Reservation(null, name, reservationDate, reservationTime);
    }

    public static Reservation assignId(Long id, Reservation reservation) {
        return new Reservation(id, reservation.getName(), reservation.getReservationDate(), reservation.getReservationTime());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
