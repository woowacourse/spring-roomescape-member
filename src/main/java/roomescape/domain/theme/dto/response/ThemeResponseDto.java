package roomescape.domain.theme.dto.response;

import roomescape.domain.theme.model.Theme;

public record ThemeResponseDto(long id, String name, String description, String thumbnail) {

    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(
            theme.getId(),
            theme.getName(),
            theme.getDescription(),
            theme.getThumbnail()
        );
    }
}
