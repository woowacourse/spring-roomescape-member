package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record AvailableTimeResponseDto(
        @JsonProperty("timeId") @NotNull Long timeId,
        @JsonProperty("startAt") @NotNull LocalTime startAt,
        @JsonProperty("alreadyBooked") @NotNull Boolean alreadyBooked) {
}
