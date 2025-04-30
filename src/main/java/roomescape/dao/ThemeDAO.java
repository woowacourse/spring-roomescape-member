package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeDAO {

    long insert(Theme theme);

    boolean existsByName(String name);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> findPopularThemes(LocalDate start, LocalDate end);

    boolean deleteById(long id);
}
