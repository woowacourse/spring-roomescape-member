package roomescape.reservation.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeSaveResponse(@NotNull Long id,
                                          @JsonFormat(pattern = "HH:mm") @NotNull LocalTime startAt) {
}
