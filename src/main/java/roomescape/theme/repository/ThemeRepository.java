package roomescape.theme.repository;

import roomescape.theme.entity.ThemeEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    ThemeEntity save(ThemeEntity entity);

    List<ThemeEntity> findAll();

    boolean deleteById(Long id);

    Optional<ThemeEntity> findById(Long id);

    Optional<ThemeEntity> findByName(String name);

    List<ThemeEntity> findPopularThemesByDateRangeAndLimit(LocalDate startDate, LocalDate endDate, int limit);
}
