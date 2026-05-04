package roomescape.reservation.presentation.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

public record ReservationSaveResponse(@NotNull Long id,
                                      @NotNull String name,
                                      @NotNull LocalDate date,
                                      @NotNull TimeInformation time) {
}
