package roomescape.dto.response;

import java.time.LocalTime;

public record ReservationTimeWithBookStatusResponse(Long timeId, LocalTime startAt, boolean alreadyBooked) {

}
