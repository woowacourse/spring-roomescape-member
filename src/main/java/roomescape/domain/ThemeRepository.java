package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.exception.EntityNotFoundException;

public interface ThemeRepository {

    Theme create(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    Theme getById(long id) throws EntityNotFoundException;

    void deleteById(long id);

    List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, int limit);

    boolean existsByTimeId(long id);
}
