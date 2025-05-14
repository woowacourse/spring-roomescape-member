package roomescape.domain.reservation.dto.theme;

import roomescape.domain.reservation.entity.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeResponse from(final Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
