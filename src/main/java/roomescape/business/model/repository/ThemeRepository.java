package roomescape.business.model.repository;

import roomescape.business.model.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int count);

    Optional<Theme> findById(long id);

    boolean existById(long id);

    void deleteById(long id);
}
