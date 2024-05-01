package roomescape.domain.theme.repository;

import java.util.List;
import roomescape.domain.theme.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(long id);
}
