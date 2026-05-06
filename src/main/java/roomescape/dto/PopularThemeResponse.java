package roomescape.dto;

import roomescape.domain.Theme;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailImageUrl,
        Long reservedCount
) {
}
