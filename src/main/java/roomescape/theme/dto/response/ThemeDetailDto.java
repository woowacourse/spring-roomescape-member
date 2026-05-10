package roomescape.theme.dto.response;

import roomescape.theme.domain.Theme;
import roomescape.theme.repository.projection.PopularThemeResult;

public record ThemeDetailDto(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        boolean isActive
) {
    public static ThemeDetailDto from(Theme theme) {
        return new ThemeDetailDto(
                theme.id(),
                theme.name(),
                theme.description(),
                theme.thumbnailUrl(),
                theme.isActive()
        );
    }

    public static ThemeDetailDto from(PopularThemeResult result) {
        return new ThemeDetailDto(
                result.id(),
                result.name(),
                result.description(),
                result.thumbnailUrl(),
                result.isActive()
        );
    }
}
