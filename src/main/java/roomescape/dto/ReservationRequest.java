package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @JsonProperty("date") @NotNull LocalDate date,
        @JsonProperty("timeId") @NotNull Long timeId,
        @JsonProperty("themeId") @NotNull Long themeId
) {
}
