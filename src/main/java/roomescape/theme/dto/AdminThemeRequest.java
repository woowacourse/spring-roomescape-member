package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminThemeRequest(
        @NotBlank
        String name,
        String description,
        String image
) {
}
