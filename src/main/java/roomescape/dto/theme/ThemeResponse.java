package roomescape.dto.theme;

import roomescape.domain.theme.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().getValue(),
                theme.getDescription().getValue(),
                theme.getThumbnail().getValue()
        );
    }

    public static ThemeResponse of(Long id, String name, String description, String thumbnail) {
        return new ThemeResponse(id, name, description, thumbnail);
    }
}
