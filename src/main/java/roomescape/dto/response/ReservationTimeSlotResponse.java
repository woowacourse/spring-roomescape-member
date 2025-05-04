package roomescape.dto.response;

import java.time.LocalTime;

public record ReservationTimeSlotResponse(Long id, LocalTime time, boolean alreadyBooked) {

}
