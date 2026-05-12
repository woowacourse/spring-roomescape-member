package roomescape.domain.reservationTime;

import java.time.LocalTime;

public record ReservationTimeWithAvailable(long id, LocalTime startAt, boolean isAvailable) {
}
