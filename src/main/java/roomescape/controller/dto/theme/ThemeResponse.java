package roomescape.controller.dto.theme;

import roomescape.domain.Theme;
import roomescape.service.dto.theme.ThemeResult;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String imageUrl
) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }

    public static ThemeResponse from(ThemeResult result) {
        return new ThemeResponse(
                result.id(),
                result.name(),
                result.description(),
                result.imageUrl()
        );
    }
}
