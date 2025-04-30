package roomescape.repository;

import java.util.List;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;

public interface ThemeRepository {
    Theme addTheme(ThemeRequestDto themeRequestDto);

    List<Theme> getAllTheme();

    void deleteTheme(Long id);

    Theme findById(Long id);
}
