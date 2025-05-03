package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.model.Theme;

public interface ThemeRepository {

    Optional<Theme> findById(final long id);

    long save(final Theme theme);

    boolean removeById(final long id);

    List<Theme> findAll();

    List<Theme> findRankingByPeriod(final LocalDate startDate, final LocalDate endDate, final int limit);
}
