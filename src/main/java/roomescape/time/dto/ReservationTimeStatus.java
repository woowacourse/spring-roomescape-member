package roomescape.time.dto;

import java.time.LocalTime;

public record ReservationTimeStatus(Long id, LocalTime startAt, Boolean alreadyBooked) {
}
