package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

public interface ThemeDao {

    Theme save(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> findPopularThemes(LocalDate from, LocalDate to, int count);

    void deleteById(long id);

    boolean existsByName(ThemeName name);
}
