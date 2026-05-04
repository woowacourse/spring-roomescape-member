package roomescape.reservation.presentation.dto.response.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeInformation(@NotNull Long id,
                              @NotNull LocalTime time) {
}
