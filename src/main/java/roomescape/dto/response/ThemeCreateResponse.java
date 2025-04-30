package roomescape.dto.response;

import roomescape.domain.Theme;

public record ThemeCreateResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeCreateResponse from(Theme theme) {
        return new ThemeCreateResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
