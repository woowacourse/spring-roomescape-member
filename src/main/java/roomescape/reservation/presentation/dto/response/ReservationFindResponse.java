package roomescape.reservation.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

public record ReservationFindResponse(@NotNull Long id,
                                      @NotNull String name,
                                      @JsonFormat(pattern = "yyyy-MM-dd") @NotNull LocalDate date,
                                      @NotNull TimeInformation time,
                                      @NotNull Long themeId) {
}
