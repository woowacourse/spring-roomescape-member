package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme findById(long id);

    Theme save(Theme theme);

    void deleteById(long id);

    List<Theme> findPopularThemes(Long topCount, LocalDate fromDate, LocalDate toDate);
}
