package roomescape.reservation.presentation.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeSaveResponse(@NotNull Long id,
                                          @NotNull LocalTime startAt) {
}
