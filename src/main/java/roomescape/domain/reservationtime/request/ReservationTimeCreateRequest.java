package roomescape.domain.reservationtime.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
