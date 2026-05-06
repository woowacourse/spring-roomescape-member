package roomescape.domain;


public class ReservationTimeAvailability {
    private final ReservationTime reservationTime;
    private final boolean isAvailable;


    public ReservationTimeAvailability(ReservationTime reservationTime, boolean isAvailable) {
        this.reservationTime = reservationTime;
        this.isAvailable = isAvailable;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
