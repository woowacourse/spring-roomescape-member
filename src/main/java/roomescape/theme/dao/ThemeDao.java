package roomescape.theme.dao;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    Theme create(Theme theme);

    void delete(Theme theme);
}
