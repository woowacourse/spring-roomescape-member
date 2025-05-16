package roomescape.theme.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeDao {

    long insert(Theme theme);

    boolean existsByName(String name);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> findPopularThemes(LocalDate start, LocalDate end, int limit);

    boolean deleteById(long id);
}
