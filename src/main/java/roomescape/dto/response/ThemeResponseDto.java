package roomescape.dto.response;

import roomescape.dao.row.ThemeRow;
import roomescape.domain.Theme;

public record ThemeResponseDto(
        Long id,
        String name,
        String thumbnailUrl,
        String description
) {
    public static ThemeResponseDto from(ThemeRow row) {
        return new ThemeResponseDto(row.id(), row.name(), row.thumbnailUrl(), row.description());
    }
    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(
                theme.getId(),
                theme.getName().value(),
                theme.getThumbnailUrl().value(),
                theme.getDescription().value()
        );
    }
}
