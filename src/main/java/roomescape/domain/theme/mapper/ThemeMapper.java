package roomescape.domain.theme.mapper;

import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.domain.theme.dto.ThemeSaveRequest;

public class ThemeMapper {

    public ThemeResponse mapToResponse(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public ThemeResponse mapToResponse(Long id, Theme theme) {
        return new ThemeResponse(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public Theme mapToTheme(ThemeSaveRequest request) {
        return new Theme(request.id(), request.name(), request.description(), request.thumbnail());
    }
}
