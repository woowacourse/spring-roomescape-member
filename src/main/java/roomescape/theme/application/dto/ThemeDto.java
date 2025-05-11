package roomescape.theme.application.dto;

import java.util.List;
import roomescape.theme.domain.Theme;

public record ThemeDto(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeDto from(Theme theme) {
        return new ThemeDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public static List<ThemeDto> from(List<Theme> themes) {
        return themes.stream()
                .map(ThemeDto::from)
                .toList();
    }
}
