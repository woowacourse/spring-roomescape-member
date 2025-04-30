package roomescape.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ThemeCreateRequest(
        @NotBlank String name,
        @NotNull String description,
        @NotNull String page,
        @NotNull String thumbnail
) {
}
