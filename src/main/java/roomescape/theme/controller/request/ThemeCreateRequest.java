package roomescape.theme.controller.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreateRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String thumbnail) {
}
