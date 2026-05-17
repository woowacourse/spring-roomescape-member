package roomescape.theme.controller.dto;

import roomescape.theme.service.dto.ThemeResult;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {

    public static ThemeResponse from(
            ThemeResult result
    ) {
        return new ThemeResponse(
                result.id(),
                result.name(),
                result.description(),
                result.thumbnailUrl()
        );
    }
}
