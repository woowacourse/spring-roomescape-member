package roomescape.time.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record ReservationTimeRequest(
        @NotNull LocalTime startAt
) {

}
