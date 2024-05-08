package roomescape.time.dto;

import java.time.LocalTime;

public record ReservationTimeStatusDto(Long id, LocalTime startAt, Boolean alreadyBooked) {
}
