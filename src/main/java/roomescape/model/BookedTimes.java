package roomescape.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookedTimes {

    private final Map<ReservationTime, Boolean> reservationsIsBooked;

    private BookedTimes(final Map<ReservationTime, Boolean> reservationsIsBooked) {
        this.reservationsIsBooked = reservationsIsBooked;
    }

    public static BookedTimes of(final List<Reservation> reservations, final List<ReservationTime> times) {
        final Map<ReservationTime, Boolean> reservationTimes = new HashMap<>();
        times.forEach(time -> reservationTimes.put(time, isAlreadyBooked(reservations, time)));

        return new BookedTimes(reservationTimes);
    }

    private static boolean isAlreadyBooked(List<Reservation> reservations, ReservationTime time) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isTime(time));
    }

    public Map<ReservationTime, Boolean> getReservationsIsBooked() {
        return Collections.unmodifiableMap(reservationsIsBooked);
    }
}
