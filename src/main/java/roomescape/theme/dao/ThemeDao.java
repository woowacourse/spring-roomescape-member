package roomescape.theme.dao;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import roomescape.theme.Theme;

public interface ThemeDao {
    Theme create(Theme theme);

    List<Theme> findAll();

    int delete(long id);

    Optional<Theme> findByName(String name);

    Optional<Theme> findById(@NotNull Long aLong);
}