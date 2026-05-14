package roomescape.controller.dto.time;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequest(@NotNull LocalTime startAt) {
}
