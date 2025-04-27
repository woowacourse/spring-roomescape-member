package roomescape.reservation.domain.aggregate;

public class Reservation {

    private final Long id;
    private final ReservationName name;
    private final ReservationDate date;
    private final ReservationTime reservationTime;

    public Reservation(final Long id, final ReservationName name, final ReservationDate date,
                       final ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
    }

    public Long getId() {
        return id;
    }

    public ReservationName getName() {
        return name;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
