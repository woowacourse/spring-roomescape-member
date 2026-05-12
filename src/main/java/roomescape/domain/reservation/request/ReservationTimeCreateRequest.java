package roomescape.domain.reservation.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
