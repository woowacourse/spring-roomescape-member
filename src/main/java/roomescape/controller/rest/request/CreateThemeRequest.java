package roomescape.controller.rest.request;

import jakarta.validation.constraints.NotBlank;

public record CreateThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String thumbnail
) {
}
