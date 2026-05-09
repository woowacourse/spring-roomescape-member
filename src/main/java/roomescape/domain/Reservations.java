package roomescape.domain;

import java.util.List;

public class Reservations {
    private final List<Reservation> reservations;

    public Reservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isAvailable(ReservationTime time) {
        return reservations.stream()
                .noneMatch(reservation -> time.equals(reservation.getTime()));
    }
}
