package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(final long id, final String name, final LocalDate date, final ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(final long id, final String name, final String date, final ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date);
        this.time = time;
    }

    public Reservation(final String name, final LocalDate date, final ReservationTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation toEntity(long id) {
        return new Reservation(id, name, date, time);
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
}
