package roomescape.theme.dto.response;

import roomescape.theme.domain.Theme;

public record CreateThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static CreateThemeResponse from(Theme theme) {
        return new CreateThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }
}
