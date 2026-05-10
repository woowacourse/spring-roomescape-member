package roomescape.service.dto;

import java.time.LocalTime;

public record CreateReservationTimeCommand(
        LocalTime startAt
) {
}
