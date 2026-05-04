package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeResponse (
        Long id,
        String name,
        String description,
        String imageUrl
) {

    public static ThemeResponse from(Theme saved) {
        return new ThemeResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getImageUrl()
        );
    }
}