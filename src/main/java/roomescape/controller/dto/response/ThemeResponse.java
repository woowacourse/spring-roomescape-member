package roomescape.controller.dto.response;

import java.util.List;
import roomescape.model.entity.Theme;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public static List<ThemeResponse> from(List<Theme> theme) {
        return theme.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
