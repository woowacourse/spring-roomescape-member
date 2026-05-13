package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.theme.Theme;

public record ThemeResponse(
        Long id,
        @NotBlank
        String name,
        String description,
        String image
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getImage());
    }
}
