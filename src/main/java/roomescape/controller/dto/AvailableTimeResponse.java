package roomescape.controller.dto;

import java.time.LocalTime;

public record AvailableTimeResponse(long id, LocalTime startAt, boolean alreadyBooked) {
}
