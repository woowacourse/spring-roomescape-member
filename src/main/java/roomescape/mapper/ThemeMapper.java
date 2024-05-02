package roomescape.mapper;

import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeRequest;

public class ThemeMapper {

    public ThemeResponse mapToResponse(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public Theme mapToTheme(ThemeRequest request) {
        return new Theme(request.id(), request.name(), request.description(), request.thumbnail());
    }
}
