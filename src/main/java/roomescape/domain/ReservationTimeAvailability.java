package roomescape.domain;

public class ReservationTimeAvailability {

    private final ReservationTime reservationTime;
    private final boolean alreadyBooked;

    public ReservationTimeAvailability(ReservationTime reservationTime, boolean alreadyBooked) {
        this.reservationTime = reservationTime;
        this.alreadyBooked = alreadyBooked;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
