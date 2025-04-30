package roomescape.dto;

import java.time.LocalTime;

public record ReservationTimeWithBookState(long id, LocalTime startAt, boolean bookState) {
}
