package roomescape.requestDto;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
