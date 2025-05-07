package roomescape.business.model.repository;

import roomescape.business.model.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    void save(Theme theme);

    List<Theme> findAll();

    List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int count);

    Optional<Theme> findById(String id);

    boolean existById(String id);

    void deleteById(String id);
}
