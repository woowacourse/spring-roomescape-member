package roomescape.domain;

import java.util.Collections;
import java.util.List;

public class Reservations {
    private final List<Reservation> reservations;

    public Reservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    public boolean hasSameReservation(Reservation otherReservation) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameReservation(otherReservation));
    }

    public boolean hasReservationTimeOf(long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isReservationTimeOf(timeId));
    }

    public boolean hasThemeOf(long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isThemeOf(themeId));
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }
}
