package roomescape.presentation.dto.response;

import java.util.List;
import roomescape.domain.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public static List<ThemeResponse> from(List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
