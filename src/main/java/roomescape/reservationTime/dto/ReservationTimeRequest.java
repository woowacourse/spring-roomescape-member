package roomescape.reservationTime.dto;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {
}
