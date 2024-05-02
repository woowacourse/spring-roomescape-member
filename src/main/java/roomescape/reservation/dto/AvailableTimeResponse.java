package roomescape.reservation.dto;

import java.time.LocalTime;

public record AvailableTimeResponse(long timeId, LocalTime startAt, boolean alreadyBooked) {
}
