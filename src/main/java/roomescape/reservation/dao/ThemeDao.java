package roomescape.reservation.dao;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;

public interface ThemeDao {

    Theme save(Theme theme);

    List<Theme> findAllThemes();

    void delete(Long id);

    Optional<Theme> findById(Long id);
}
