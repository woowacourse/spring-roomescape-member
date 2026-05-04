package roomescape.repository.theme;

import roomescape.domain.Theme;

import java.util.List;

public interface ThemeRepository {

    Theme createTheme(Theme theme);

    void deleteById(Long id);

    List<Theme> findAll();

    Theme findById(Long id);
}
