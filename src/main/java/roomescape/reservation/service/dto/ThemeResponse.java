package roomescape.reservation.service.dto;

import roomescape.reservation.domain.Theme;

public record ThemeResponse(long id, String name, String description, String thumbnail) {
    public ThemeResponse(Theme theme) {
        this(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
