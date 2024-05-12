package roomescape.dto.reservation.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record ReservationAvailableTimeResponse(
        Long timeId,
        @JsonFormat(pattern = "HH:mm") LocalTime startAt,
        boolean alreadyBooked
) {
}
