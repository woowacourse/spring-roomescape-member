package roomescape.theme.service;

import java.util.List;

import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.ThemeSaveServiceDto;

public interface ThemeService {
    List<Theme> getAll();
    Theme create(ThemeSaveServiceDto theme);
    void deleteById(Long id);
    List<Theme> getBestThemes();
}
