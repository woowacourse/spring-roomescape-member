package roomescape.theme.repository;

import java.util.List;
import roomescape.theme.model.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);
}
