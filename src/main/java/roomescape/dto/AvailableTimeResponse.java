package roomescape.dto;

import java.time.LocalTime;

public record AvailableTimeResponse(LocalTime time, Long timeId, boolean alreadyBooked) {
}
