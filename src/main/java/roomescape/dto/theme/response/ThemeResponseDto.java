package roomescape.dto.theme.response;

import roomescape.domain.theme.Theme;

public record ThemeResponseDto(Long id, String name, String description, String thumbnail) {

    public static ThemeResponseDto from(final Theme theme) {
        return new ThemeResponseDto(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
