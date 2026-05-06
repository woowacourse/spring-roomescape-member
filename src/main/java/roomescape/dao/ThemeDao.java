package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {
    Theme create(Theme theme);

    Theme read(Long id);

    List<Theme> readAll();

    void delete(Long id);

    List<Theme> readRanking(LocalDate startDate, LocalDate endDate, int limit);
}
