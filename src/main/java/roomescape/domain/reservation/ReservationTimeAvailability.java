package roomescape.domain.reservation;

import java.util.Objects;

public class ReservationTimeAvailability {

    private final ReservationTime reservationTime;
    private final boolean alreadyBooked;

    public ReservationTimeAvailability(ReservationTime reservationTime, boolean alreadyBooked) {
        this.reservationTime = reservationTime;
        this.alreadyBooked = alreadyBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationTimeAvailability that = (ReservationTimeAvailability) o;

        if (alreadyBooked != that.alreadyBooked) return false;
        return Objects.equals(reservationTime, that.reservationTime);
    }

    @Override
    public int hashCode() {
        int result = reservationTime != null ? reservationTime.hashCode() : 0;
        result = 31 * result + (alreadyBooked ? 1 : 0);
        return result;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
