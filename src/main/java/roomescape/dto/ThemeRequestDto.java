package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ThemeRequestDto(
        @JsonProperty("name") @NotNull String name,
        @JsonProperty("description") @NotNull String description,
        @JsonProperty("thumbnail") @NotNull String thumbnail
) {
}

