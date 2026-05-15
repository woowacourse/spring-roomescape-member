package roomescape.theme.dto;

import jakarta.validation.constraints.NotEmpty;

public record AdminThemeRequest(
        @NotEmpty
        String name,
        String description,
        String image
) {
}
