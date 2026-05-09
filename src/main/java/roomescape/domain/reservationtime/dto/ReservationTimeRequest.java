package roomescape.domain.reservationtime.dto;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {
}
