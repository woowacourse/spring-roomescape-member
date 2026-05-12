package roomescape.domain.reservationTime;

import java.time.LocalTime;

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }
}
