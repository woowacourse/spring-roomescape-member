package roomescape.domain.time.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
