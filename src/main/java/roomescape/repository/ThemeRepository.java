package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();

    void deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findTopByReservationCountAsc(Long listNum);

    List<Theme> findTopByReservationCountDesc(Long listNum);
}
