package roomescape.business.model.repository;

import roomescape.business.model.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    Optional<Theme> findById(long id);

    List<Theme> findAll();

    boolean existById(long id);

    List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int count);

    void deleteById(long id);
}
