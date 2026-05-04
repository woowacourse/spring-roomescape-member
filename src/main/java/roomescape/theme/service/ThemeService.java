package roomescape.theme.service;

import java.util.List;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.ThemeSaveServiceDto;

public interface ThemeService {
    List<Theme> getThemes();
    Theme save(ThemeSaveServiceDto theme);
    void deleteById(long id);
}
