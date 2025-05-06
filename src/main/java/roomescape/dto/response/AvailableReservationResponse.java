package roomescape.dto.response;

import java.time.LocalTime;

public record AvailableReservationResponse(
        Long timeId,
        LocalTime startAt,
        Boolean isBooked
) {
}
