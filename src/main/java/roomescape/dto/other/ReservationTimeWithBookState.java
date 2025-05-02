package roomescape.dto.other;

import java.time.LocalTime;

public record ReservationTimeWithBookState(long id, LocalTime startAt, boolean bookState) {

}
