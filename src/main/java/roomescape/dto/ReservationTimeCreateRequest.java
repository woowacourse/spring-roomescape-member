package roomescape.dto;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
