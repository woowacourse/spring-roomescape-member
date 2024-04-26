package roomescape.dto.web;

import java.time.LocalTime;

public record ReservationTimeWebRequest(Long id, LocalTime startAt) {
}
