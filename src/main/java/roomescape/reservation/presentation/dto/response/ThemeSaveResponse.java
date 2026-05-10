package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.domain.Theme;

public record ThemeSaveResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
    public static ThemeSaveResponse from(Theme theme) {
        return new ThemeSaveResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl()
        );
    }
}
