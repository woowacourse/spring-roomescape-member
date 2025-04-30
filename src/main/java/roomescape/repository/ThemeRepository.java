package roomescape.repository;

import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;

public interface ThemeRepository {
    Theme addTheme(ThemeRequestDto themeRequestDto);
}
