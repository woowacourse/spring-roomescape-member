package roomescape.dao;

import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeDao {

    Theme save(final Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(final Long id);

    void deleteById(final Long id);

    List<Theme> findTopThemesByReservationCountDuringPeriod(final int period, final int limit);
}
