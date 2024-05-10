package roomescape.repository;

import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    void fetchById(long id);

    List<Theme> findPopularThemes(LocalDate from, LocalDate to, int limit);

    Theme save(Theme theme);

    int delete(long id);
}
