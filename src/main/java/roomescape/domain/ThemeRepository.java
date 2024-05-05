package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme create(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    void deleteById(long id);

    List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, int limit);

    boolean existsByTimeId(long id);
}
