package roomescape.dto.response;

import roomescape.domain.reservation.Theme;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {
    public ThemeResponse(final Theme theme) {
        this(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
