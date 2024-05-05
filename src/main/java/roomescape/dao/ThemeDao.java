package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.Theme;

public interface ThemeDao {

    List<Theme> readAll();

    Optional<Theme> readById(Long id);

    Theme create(Theme theme);

    Boolean exist(long id);

    void delete(long id);
}
