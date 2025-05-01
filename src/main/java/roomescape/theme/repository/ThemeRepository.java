package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    void deleteById(Long id);

    long countByName(String name);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findTop10ThemesByReservationCountWithin7Days();
}
