package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull LocalDate date,
        @JsonProperty("memberId") @NotNull Long userId,
        @NotNull Long themeId,
        @NotNull Long timeId
) {
}
