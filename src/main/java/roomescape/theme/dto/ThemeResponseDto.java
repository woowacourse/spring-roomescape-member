package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ThemeResponseDto(long id, String name, String description, String thumbnail) {
    
    public ThemeResponseDto(final Theme theme) {
        this(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
