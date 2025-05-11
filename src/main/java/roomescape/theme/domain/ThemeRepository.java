package roomescape.theme.domain;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Long save(Theme theme);

    void deleteById(Long id);

    Boolean existsByName(String name);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findTop10ThemesByReservationCountWithin7Days();
}
