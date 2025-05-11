package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UserReservationRequest(
        @JsonProperty("date") @NotNull LocalDate date,
        @JsonProperty("timeId") @NotNull Long timeId,
        @JsonProperty("themeId") @NotNull Long themeId
) {
    public static UserReservationRequest of(LocalDate date, Long timeId, Long themeId) {
        return new UserReservationRequest(date, timeId, themeId);
    }
}
