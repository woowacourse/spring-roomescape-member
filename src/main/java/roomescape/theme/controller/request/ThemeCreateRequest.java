package roomescape.theme.controller.request;

import jakarta.validation.constraints.NotNull;

public record ThemeCreateRequest(
        @NotNull String name,
        @NotNull String description,
        @NotNull String thumbnail
) {
}
