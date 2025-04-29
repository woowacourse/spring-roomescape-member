package roomescape.repository.theme;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme add(Theme theme);

    List<Theme> findAll();

    int deleteById(Long id);
}
