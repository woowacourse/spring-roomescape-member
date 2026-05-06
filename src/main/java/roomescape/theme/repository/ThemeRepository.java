package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    void deleteById(long id);

    Theme save(Theme theme);

    boolean existsByName(String name);

    List<Theme> findPopularThemes(int period, int limit, LocalDate now);

}
