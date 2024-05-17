package roomescape.time.dto;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
