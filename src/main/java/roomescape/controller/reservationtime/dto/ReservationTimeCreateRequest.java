package roomescape.controller.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
}
