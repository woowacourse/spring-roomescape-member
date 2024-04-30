package roomescape.domain.reservation;

import roomescape.domain.reservationtime.ReservationTime;

public class Reservation {

    private final Long id;
    private final ReservationName reservationName;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;

    public Reservation(Long id,
                       ReservationName reservationName,
                       ReservationDate reservationDate,
                       ReservationTime reservationTime) {
        this.id = id;
        this.reservationName = reservationName;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public boolean isToday() {
        return reservationDate.isToday();
    }

    public Long getId() {
        return id;
    }

    public ReservationName getName() {
        return reservationName;
    }

    public ReservationDate getDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
