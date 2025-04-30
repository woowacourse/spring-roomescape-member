package roomescape.theme.dao;

import java.util.List;
import java.util.Optional;
import roomescape.theme.Theme;

public interface ThemeDao {
    Theme create(Theme theme);

    List<Theme> findAll();

    void delete(long id);

    Optional<Theme> findByName(String name);
}