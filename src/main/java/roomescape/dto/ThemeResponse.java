package roomescape.dto;

import roomescape.domain_entity.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public ThemeResponse(Theme theme) {
        this(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
