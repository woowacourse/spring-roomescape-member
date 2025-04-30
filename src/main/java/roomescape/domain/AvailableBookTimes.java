package roomescape.domain;

import java.util.ArrayList;
import java.util.List;

public class AvailableBookTimes {

    private final List<ReservationTime> times;

    public AvailableBookTimes(List<ReservationTime> times, List<Reservation> alreadyReservedReservations) {
        List<ReservationTime> availableBookTimes = new ArrayList<>(times);
        List<ReservationTime> reservationTimeStream = alreadyReservedReservations.stream()
                .map(Reservation::getReservationTime)
                .toList();

        availableBookTimes.removeAll(reservationTimeStream);
        this.times = availableBookTimes;
    }
}
