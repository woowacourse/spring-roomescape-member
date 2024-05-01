package roomescape.reservation.dto.response;

import java.time.LocalTime;

public record FindAvailableTimesResponse(Long id, LocalTime startAt, Boolean alreadyBooked) {
}
