package roomescape.domain;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ReservationTimes {
    private final List<ReservationTime> reservationTimes;

    public ReservationTimes(List<ReservationTime> reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    public List<ReservationTime> getReservationTimes() {
        return Collections.unmodifiableList(reservationTimes);
    }
}
