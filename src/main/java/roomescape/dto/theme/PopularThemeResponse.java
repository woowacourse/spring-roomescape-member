package roomescape.dto.theme;

import roomescape.domain.theme.ThemeWithCount;

public record PopularThemeResponse(long id, String name, String description, String imageUrl, long count) {

    public static PopularThemeResponse from(ThemeWithCount themeWithCount) {
        return new PopularThemeResponse(themeWithCount.id(), themeWithCount.name(), themeWithCount.description(), themeWithCount.imageUrl(), themeWithCount.count());
    }
}
