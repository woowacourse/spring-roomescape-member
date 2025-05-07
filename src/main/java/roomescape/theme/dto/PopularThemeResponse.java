package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record PopularThemeResponse(String name, String description, String thumbnail) {
    public static PopularThemeResponse of(Theme theme) {
        return new PopularThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
