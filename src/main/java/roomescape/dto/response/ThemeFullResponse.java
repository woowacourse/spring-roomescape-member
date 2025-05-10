package roomescape.dto.response;

import roomescape.entity.Theme;

public record ThemeFullResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public ThemeFullResponse(Theme theme) {
        this(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
