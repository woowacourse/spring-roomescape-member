package roomescape.repository;

import roomescape.entity.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {

    Theme save(Theme theme);

    Theme findById(long id);

    List<Theme> findAll();

    boolean existById(long id);

    List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int count);

    void deleteById(long id);
}
