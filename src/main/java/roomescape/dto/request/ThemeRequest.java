package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;

public record ThemeRequest(
        @NotNull String name,
        @NotNull String description,
        @NotNull String thumbnail
) {

}
