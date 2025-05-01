package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;

public interface ThemeRepository {

    Long saveAndReturnId(Theme theme);

    List<Theme> findAll();

    int deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findByPeriodAndLimit(LocalDate start, LocalDate end, int limit);
}
