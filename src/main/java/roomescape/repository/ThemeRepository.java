package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.PopularTheme;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll(int limit, int offset);

    Theme findById(Long id);

    Long save(Theme theme);

    void deleteById(Long id);

    List<PopularTheme> findPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit);
}
