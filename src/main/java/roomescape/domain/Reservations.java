package roomescape.domain;

import java.util.List;

public class Reservations {
    private final List<Reservation> reservations;

    public Reservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean hasReservationOn(ReservationDate date) {
        return reservations.stream()
                .anyMatch(r -> r.getDate().equals(date));
    }

    public boolean hasReservationOnExcluding(ReservationDate date, long excludeId) {
        return reservations.stream()
                .filter(r -> r.getId() != excludeId)
                .anyMatch(r -> r.getDate().equals(date));
    }
}
